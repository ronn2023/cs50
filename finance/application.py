import os
from datetime import datetime
from pytz import timezone
import pytz
from cs50 import SQL
from flask import Flask, flash, redirect, render_template, request, session
from flask_session import Session
from tempfile import mkdtemp
from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError
from werkzeug.security import check_password_hash, generate_password_hash

from helpers import apology, login_required, lookup, usd



# Configure application
app = Flask(__name__)

# Ensure templates are auto-reloaded
app.config["TEMPLATES_AUTO_RELOAD"] = True


# Ensure responses aren't cached
@app.after_request
def after_request(response):
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response


# Custom filter
app.jinja_env.filters["usd"] = usd

# Configure session to use filesystem (instead of signed cookies)
app.config["SESSION_FILE_DIR"] = mkdtemp()
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///finance.db")

# Make sure API key is set
if not os.environ.get("API_KEY"):
    raise RuntimeError("API_KEY not set")


@app.route("/", methods=["GET", "POST"])
@login_required
def index():
    if request.method == "POST":
        #if program comes here, the button for resetting the user's data has been pressed
        db.execute("DELETE FROM userledger WHERE username = ?;", session["user_name"])
        db.execute("UPDATE users SET cash = ? WHERE username = ?;", 10000, session["user_name"])
        row = db.execute("SELECT * FROM users WHERE username = ?;", session["user_name"])
        return render_template("index.html", ledgers = {}, cash = row[0]['cash'], userMoney = row[0]['cash'], totalMoney = row[0]['cash'])
    else:
        #grab user data and the ledger of transactions
        userData = db.execute("SELECT * FROM users WHERE username = ?;", session["user_name"])
        ledger = db.execute("SELECT * FROM userledger WHERE username = ?;", session["user_name"])
        total = 0
        #calculate how much money has been made or lost in buying stocks
        for row in ledger:
            total += row['OstockPrice'] * row['numStocks']
            if row['sold'] == "Sold":
                continue
            db.execute("UPDATE userledger SET curStockPrice = ? WHERE id = ?;", lookup(row['stockSymbol'])['price'], row['id'])
            """Show portfolio of stocks"""
            
        #pass necessary data to index.html file
        cash = userData[0]['cash']
        total = round(total, 2)
        total *= -1
        return render_template("index.html", ledgers = ledger, cash = round(cash, 2), userMoney = userData[0]['cash'] , totalMoney = cash + total)


@app.route("/buy", methods=["GET", "POST"])
@login_required
def buy():
    """Buy shares of stock"""
    if request.method == "GET":
        #render template
        return render_template("buy.html")
    elif request.method == "POST" and not lookup(request.form.get("symbol")) is None:
        #check for invalid input(HTML file takes care of making sure you cannot submit fractional values )
        if int(request.form.get("shares")) <=0 or "/" in request.form.get("shares") or "." in request.form.get("shares"):
            return apology("Invalid number of stocks")
        #grab data
        rows = db.execute("SELECT * FROM users WHERE username = ?;", session["user_name"])
        
        #ensure user can afford stocks
        if lookup(request.form.get("symbol"))['price'] * int(request.form.get("shares")) > rows[0]['cash']:
            return apology("You don't have enough cash!")
        date = datetime.now(tz=pytz.utc)
        date = date.astimezone(timezone('US/Pacific'))
        
        #update database
        stock = db.execute("SELECT * FROM userledger WHERE stockSymbol = ? AND username - ?;", request.form.get("symbol"), session["user_name"])

        price = lookup(request.form.get("symbol"))['price']
        db.execute("INSERT INTO userledger (username, stockSymbol, stockName, OstockPrice, curStockPrice, date, numStocks, sold) VALUES (?,?,?,?,?,?,?,?);", session["user_name"], request.form.get("symbol"), lookup(request.form.get("symbol"))['name'], price,price, str(date),request.form.get("shares"), "Bought")
        db.execute("UPDATE users SET cash = ? WHERE username = ?;", rows[0]['cash']-(lookup(request.form.get("symbol"))['price'] * int(request.form.get("shares"))), session["user_name"])
        return redirect("/")

    return apology("Incorrect Stock Name")


@app.route("/history")
@login_required
def history():
    """Show history of transactions"""
    history = db.execute("SELECT * FROM userledger WHERE username = ?", session["user_name"])

    if len(history) == 0:
        return apology("No Available History: Buy Stocks First!")
    else:
        #pass database to history.html
        return render_template("history.html", history = history)


@app.route("/login", methods=["GET", "POST"])
def login():
    """Log user in"""

    # Forget any user_id
    session.clear()

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure username was submitted
        if not request.form.get("username"):
            return apology("must provide username", 403)

        # Ensure password was submitted
        elif not request.form.get("password"):
            return apology("must provide password", 403)

        # Query database for username
        rows = db.execute("SELECT * FROM users WHERE username = ?", request.form.get("username"))

        # Ensure username exists and password is correct
        if len(rows) != 1 or not check_password_hash(rows[0]["hash"], request.form.get("password")):
            return apology("invalid username and/or password", 403)

        # Remember which user has logged in
        session["user_id"] = rows[0]["id"]
        session["user_name"] = rows[0]["username"]


        # Redirect user to home page
        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("login.html")


@app.route("/logout")
def logout():
    """Log user out"""

    # Forget any user_id
    session.clear()

    # Redirect user to login form
    return redirect("/")



@app.route("/quote", methods=["GET", "POST"])
@login_required
def quote():
    """Get stock quote."""
    if request.method == "GET":
        #render the quote.html file
        return render_template("quote.html")
    elif request.method == "POST" and not lookup(request.form.get("symbol")) == None:
        #looks up the information on the stock and passes it to the quote.html file
        value = float(lookup(request.form.get("symbol"))['price'])
        return render_template("quote.html", symbol = lookup(request.form.get("symbol"))['symbol'], name = lookup(request.form.get("symbol"))['name'], price = "${:,.2f}".format(value))
    return apology("Invalid Stock", 400)


@app.route("/register", methods=["GET", "POST"])
def register():
    """Register user"""

    if request.method == "GET":
        return render_template("register.html")

    elif request.method == "POST":
        #error checking
        if not request.form.get("username") or not request.form.get("password"):
            return apology("Please input username and password")
        rows = db.execute("SELECT * FROM users WHERE username = ?;", request.form.get("username"))
        #more error checking
        if len(rows) >=1:
            return apology("Username is already being used by another person")
        #insert user information into database
        if request.form.get("password") == request.form.get("confirmation") and request.form.get("username") and request.form.get("password"):
            db.execute("INSERT INTO users (username, hash) VALUES (?,?);", request.form.get("username"), generate_password_hash(request.form.get("password")))
            return render_template("login.html")
        else:
            return apology("Passwords do not match")
    return redirect("/register")


@app.route("/sell", methods=["GET", "POST"])
@login_required
def sell():
    shares = db.execute("SELECT * FROM userledger WHERE username = ?;", session["user_name"])
    if request.method == "GET":
        return render_template("sell.html", shares = shares)
    """Sell shares of stock"""
    if request.method == "POST":
        #ensure proper user input
        if int(request.form.get("shares")) == 0 or not request.form.get("shares"):
            return apology("Invalid number of stocks")

        totalMoney = 0
        totalStocks = 0
        
        #data
        rows = db.execute("SELECT * FROM userledger WHERE stockSymbol = ? AND username = ?;", request.form.get("symbol"), session["user_name"])
        
        #iterate over data to find out how many stocks exist in the users posession
        for row in rows:
            totalStocks += row['numStocks']
        if int(request.form.get("shares")) <= totalStocks:
            counter = 0
            for row in rows:
                if counter <= int(request.form.get("shares")):
                    totalMoney = totalMoney + int(row['curStockPrice']) * int(row['numStocks'])
                    counter = counter + 1
            #update database
            price = lookup(request.form.get("symbol"))['price']
            date = datetime.now(tz=pytz.utc)
            date = date.astimezone(timezone('US/Pacific'))
            db.execute("INSERT INTO userledger (username, stockSymbol, stockName, OstockPrice, curStockPrice, date, numStocks, sold) VALUES (?,?,?,?,?,?,?,?);", session["user_name"], request.form.get("symbol"), lookup(request.form.get("symbol"))['name'], price,price, str(date), -int(request.form.get("shares")), "Sold")
            users = db.execute("SELECT * FROM users WHERE username = ?;", session["user_name"])
            db.execute("UPDATE users SET cash = ? WHERE username = ?;", users[0]['cash'] + price * int(request.form.get("shares")), session["user_name"])
            return redirect("/")
    return apology("Not enough stocks!")


def errorhandler(e):
    """Handle error"""
    if not isinstance(e, HTTPException):
        e = InternalServerError()
    return apology(e.name, e.code)


# Listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)

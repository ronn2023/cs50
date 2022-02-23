from flask import Flask,redirect, render_template, request
from cs50 import SQL
app = Flask(__name__)

REGISTRANTS = {}

db = SQL("sqlite:///froshims.db")

SPORTS = [
    "dodgeball", "volleyball", "ultimate frisbee", "soccer"
]

@app.route("/")
def index():
    return render_template("index.html", sports = SPORTS)


@app.route("/register" , methods = ["POST"])
def register():
    name = request.form.get("name")
    sport = request.form.get("sport")
    if not name and not sport:
        return render_template("error.html", message = "Please input a name and sport!")
    if not name:
        return render_template("error.html", message = "Please input a name!")
    if not sport:
        return render_template("error.html", message = "Please select a sport!")
    if sport not in SPORTS:
        return render_template("error.html", message = "Please select one of the OFFERED sports")

    REGISTRANTS[name] = sport
    print(REGISTRANTS)
    return redirect("/registrants")

@app.route("/registrants")
def registrants():
    return render_template("registrants.html", registrants = REGISTRANTS)




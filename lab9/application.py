import os

from cs50 import SQL
from flask import Flask, flash, jsonify, redirect, render_template, request, session

# Configure application
app = Flask(__name__)

# Ensure templates are auto-reloaded
app.config["TEMPLATES_AUTO_RELOAD"] = True

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///birthdays.db")




@app.route("/", methods=["GET", "POST"])
def index():
    #grab all queries in data
    birthdays = db.execute("SELECT * FROM birthdays")


    if request.method == "POST":

        #check if user has signed in and delete submissions
        if request.form.get("adminPass") == "Password":
            db.execute("DELETE FROM birthdays")
            return render_template("index.html", birthdays = {})
        if request.form.get("adminPass"):
            return render_template("index.html", birthdays = birthdays)

        #variables holding user input
        name = str(request.form.get("name"))
        month = int(request.form.get("month"))
        day = int(request.form.get("day"))

        #checking for valid input and storing valid data into database
        if isinstance(name, str):
            db.execute("INSERT INTO birthdays (name, month, day) VALUES (?,?,?);", name,month,day)
        # TODO: Add the user's entry into the database

        return redirect("/")

    else:

        # TODO: Display the entries in the database on index.html
        # birthdays = jsonify(birthdays)

        return render_template("index.html", birthdays = birthdays)





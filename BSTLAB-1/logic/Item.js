const {
   builtinModules
} = require("module");

class Item {
   #name = ''; // Cannot be null
   #stock = 0; // All values allowed
   #cost = 0.0; // Cannot be negative


   /**
    * constructor to create an Item object with fields...
    * @param {*} name : the name of the object, passed in main as the inventory file is read
    * @param {*} stock : the stock number of the object, passed in main as the inventory file is read
    * @param {*} cost : the cost of the object, passed in main as the inventory file is read
    * all params have default values if nothing is passed into the fields of the constructor
    */
   constructor(name = '', stock = 0, cost = 0) {
      //if theres no name, set it equal to an empty string 
      if (!name) name = '';

      //otherwise set name to the passed name parameter
      this.#name = name;

      //ensures cost cannot be null, negative, or a non number
      if (cost != null && cost >=0 && typeof cost == "number"){
         this.#cost = cost;

      }
      //ensures stock cannot be null or a non number
      if (stock != null && typeof stock == "number"){
         this.#stock = stock;

      }

   }
   // Add Appropriate getters and setters

   
   /**
    * getter for cost returns cost of Current item
    */
   get cost() {
      return this.#cost;

   }

   /**
    * method returns JSON of item in valid format
    */
   giveJSON() {
      let stock = this.#stock;
      console.log(typeof stock + "--------------------")
      stock = stock.toString();
      console.log(stock + "*************************")
      
      let cost = Math.round(100*this.#cost)/100;
      cost = cost.toString();
      cost = "$" + cost
      let JSONITEM = {
         "name": this.#name,
         "stock": stock,
         "cost": cost
      }
      return JSONITEM;
   }
  

   /**
    * getter for stock returns eturns stock of current item
    **/
   get stock() {
      return this.#stock;

   }

   /**
    * getter for name returns name of current item
    * */
   get name() {
      return this.#name;

   }
}
module.exports = Item
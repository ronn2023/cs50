const e = require('express');
const fs = require('fs');
const readline = require('readline');
const BST = require('./BST.js')
const Item = require('./Item.js')

async function processLineByLine() {
    const fileStream = fs.createReadStream('inventory.txt');

    const rl = readline.createInterface({
        input: fileStream,
        crlfDelay: Infinity
    });
    // Note: we use the crlfDelay option to recognize all instances of CR LF
    // ('\r\n') in input.txt as a single line break.

    let tree = new BST( function compare (Item1, Item2){

            if (Item1 != null && Item2 != null){
                return Item1.name.localeCompare(Item2.name)
            }
            else if (Item1 == null && Item2 != null){
                return -1;
            }
            else if (Item1 != null && Item2 == null){
                return 1;
            
            }
            else if (Item1 == null && Item2 == null){
                return 0;
            }
        }
    )
  
    
    for await (const line of rl) {
        // Each line in input.txt will be successively available here as `line`.
        // console.log(`Line from file: ${line}`);
        let fileLine = JSON.parse(line)
        
        //store data of each line as valid arguments for an Item
        let name = fileLine.name
        let stock = fileLine.stock
        let cost = parseFloat(fileLine.cost.substring(1));

        //create Item object from data
        let itemfromLine = new Item(name, stock, cost)
        
        //check for duplicate name in the tree, if theres a duplicate, it will be deleted from the tree
        let duplicate = tree.remove(itemfromLine)
        
        //if there's a duplicate: create a new Item with the average of the cost of the duplicate and the item from the line,
        // the sum the two's stocks, and add the Item to the tree
        if (duplicate != null){
            count = count + 1;
            
            let avg = (cost + duplicate.cost)/2;
            let itemToInsert = new Item(name, itemfromLine.stock + duplicate.stock, avg)
            
            tree.add(itemToInsert);
            
        }
        else{
            //if no duplicate, add the item from the line directly into tree
            tree.add(itemfromLine)
        }
    }
    //store items from the tree inOrder by Alphabetical order per the comparator
    let arr = tree.inOrder();
    
    //convert array into proper output syntax and display output in a new file called "storeData.txt"
    let finalArr = ""
    // console.log(count)
    arr.forEach(function(item, index){
        if (arr[index].stock >= 0){
            finalArr = finalArr + JSON.stringify(arr[index].giveJSON()) + '\n';
        }
    })

    finalArr = finalArr + ""
    
    
    fs.writeFile("storeData.txt", finalArr, function(err) {
        if(err) {
            return console.log(err);
        }
        console.log("The file was saved!");
    });
    
}
processLineByLine();


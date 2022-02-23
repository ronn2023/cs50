const e = require("express");
const { builtinModules } = require("module");


class BST {
    #root;
   
    // default comparator must be a function(a,b)
    // which returns:
    // < 0 if a < b
    // = 0 if a == b
    // > 0 if a > b
    #comparator = function (a, b) {
        throw 'Comparator not defined!';
    }
    static BSTNode = class {
        

        constructor(data) {
            //data element
           
            this.data = data
            

            //left and right nodes
            this.left = null;
            this.right = null;
        }
       

    }


    // comparator(a,b) return negative,0,positive (a<b, a==b, a>b)
    constructor(comparator) {
        this.#root = null
        if (comparator){
            this.#comparator = comparator;
        }
        
    }
    
    /**
     * Adding a node to the bst
     **/
    add(data) {
        //make sure client cannot input invalid data
        if (data == null || data == "") {
           
            return false;
        }
        let n = new BST.BSTNode(data)
        
        let current = this.#root
        if (current == null) { //BST is empty
            this.#root = n //points head to n

        } else {
            //slow pointer that serves as the parent of the current node
            let prev = null

            while (current != null) {
                //if the data that's being attempted to add to BST is less than the current node's data, go to the left
                //else go to right
                if (this.#comparator(data, current.data) <= 0) {
                    prev = current
                    current = current.left;

                } else {
                    prev = current
                    current = current.right

                }
            }


            //use comparator to add node to proper right/left attribute of the prev node 

            if (this.#comparator(data, prev.data) <= 0) {
                prev.left = n
                return true;
            } else {
                prev.right = n;
                return true;
            }
            

        }
    }
    /**
     * Removing a node from the bst based on the data and returning the data that has 'replaced' it
     * returns null if you attempt to enter data that is not in the tree
     * removing the first instance of data found on the tree
     * data: the data you want to remove
     * 
     **/
    remove(data) {
        //function to handle removing a node with one or two children
        let removeNode = function(parent, remove){
            
        
            let isLeftChild = true;
            if (parent.right == remove){
                isLeftChild = false;
            } 

            let pointHere = remove.left;
            if (remove.left == null){
                pointHere = remove.right;
            }
            if (isLeftChild){
                parent.left = pointHere;
            } else{
                parent.right = pointHere;
            }
            //return removed data
            return remove.data;

        }

        //variables to store the parent and current node
        let p = null;
        let r = this.#root;
    
       
        while (r != null){
            //find parent of the node to be removed as well as the node to be removed
            if (this.#comparator(r.data, data) < 0){
                p = r;
                r = r.right;
            
            }
            else if (this.#comparator(r.data, data) > 0){
                p = r;
                r = r.left;
               
            } 
            else{
                //handle removing root
                if (p == null){
                    //if root has children, go to its left child and then find the right most node of the left child
                    if (r.left != null && r.right != null){
                        //move left of parent
                        let rp = r;
                        let removeOne = rp.left;

                        //find rightmost child of left node
                        while (removeOne.right != null){
                            rp = removeOne;
                            removeOne = removeOne.right
                        }
                        //swap data of node to be removed and rightmost child
                        //then use removeNode() and pass in the nodes of the parent and node to be removed
                        let t = r.data;
                        r.data = removeOne.data
                        removeOne.data = t;
                        return removeNode(rp, removeOne);
                    }
                    //if root doesn't have children, delete it and return its data
                    let temp = this.#root.data;
                    this.#root = null;

                    //return removed data;
                    return temp;

                }
                else{
                    //deleting node that is not the root
                    if (r.left != null && r.right != null){

                        //move left of parent
                        let rp = r;
                        let removeOne = rp.left;

                        //find rightmost child
                        while (removeOne.right != null){
                            rp = removeOne;
                            removeOne = removeOne.right
                        }
                        //swap data of node to be removed and rightmost child
                        //then use removeNode() and pass in the nodes of the parent and node to be removed
                        let t = r.data;
                        r.data = removeOne.data
                        removeOne.data = t;
                        return removeNode(rp, removeOne);
                    }
                    else{
                        //if 0 children, removeNode() takes the original parent and the remove node
                        return removeNode(p, r)
                    }
                }
            }
        }
        
        return null;

        
        
    }



    /**
     * recursive inOrder traversal of BST to sort by order enforced by comparator
     */
    inOrder() {
        //the recursive function implementing LCR
        let inOrderHelper = function (n, curr) {
            if (n == null) {
                return;
            }
            inOrderHelper(n.left, curr);
            curr.push(n.data);
            inOrderHelper(n.right, curr);
        }
        let rtn = [];
        inOrderHelper(this.#root, rtn);
        return rtn;
    }

    
}

module.exports = BST
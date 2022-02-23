package org.headroyce.ronn2023;

import java.util.Iterator;

/**
 * @author Brian Sea
 *
 * An iterable sigularly linked list data structure
 * @param <T> the type of data stored in the list
 */
public class LList<T> implements Iterable<T>{

    // attribute that keeps track of the size of the list
    private int size;

    //nodes used to manipulate the list
    private Node<T> curr;
    private Node<T> head;
    private Node<T> tail;

    /**
     * Initializes an empty list
     */
    public LList(){
        //set values of size, curr, head, and next to correspond to an empty list
        this.size = 0;
        this.curr = null;
        this.head = null;
        this.tail = null;


    }

    /**
     * time complexity: o(N)
     * space complexity: o(1)
     * Gets the size of the linked list.  Size is a read-only attribute.
     * @return the number of elements in the list
     */
    public int size(){

        int count = 0;
        curr = head;

        //if head is pointing to null, list is empty
        if (head == null){
            return 0;
        }
        //if head isn't pointing to null, loop through the list and count the number of nodes in the list
        else{
            while(curr != null){
                curr = curr.next;
                count++;

            }

            return count;
        }


    }

    /**
     * Add to the end of the linked list
     * @param data the data to add to the list
     * @return true on success, false otherwise
     * time complexity: o(1)
     * space complexity: o(1)
     */
    public boolean add( T data ){
        //check for invalid data
        if (data == null) {
            return false;
        }

        //create node to hold data to be added to list
        Node<T> newData = new Node<T>(data);


        //checks if list is empty and adds node to populate list
        if (head == null){
            head = newData;
            curr = head;
            tail = head;
            this.size++;
            return true;
        }

        //adds new Node to end of list
        curr.next = newData;
        tail = tail.next;
        curr = curr.next;
        this.size++;
        return true;
    }

    /**
     * Inserts data into the list after the index provided.
     * @param data the data to insert into the linked list
     * @param place the index to insert after. -1 indicates before head, > size indicates at the end of the list
     * @return true on success, false otherwise
     * time complexity: o(N)
     * space complexity: o(1)
     */
    public boolean insert( T data, int place ){
        //check for invalid input
        if (place < -1 || place > size -1 || data == null){
            return false;
        }

        //new node to store data
        Node<T> newNode = new Node<T>(data);

        //calls add function if list is empty
        if (curr == null){
            this.add(data);
            return true;
        }

        //inserting to head
        if (place == -1){
            newNode.next = head;
            head = newNode;
            this.size++;
            return true;
        }

        //inserting to tail of list
        if (place >= size - 1) {
            this.add(data);
            this.size++;
            return true;
        }
        //inserting after a node that's in between the head and tail
        curr = head;
        for (int i = 0; i < place; i++){
            curr = curr.next;
        }
        newNode.next = curr.next;
        curr.next = newNode;
        this.size++;

        return true;
    }

    /**
     * Removes an element from the list at the index provided and updates size attribute accordingly.
     * @param place index to remove; <= 0 indicates removal of first element; > size indicates removal of last element
     * @return the data that was removed
     * time complexity: o(N)
     * space complexity: o(1)
     */
    public T remove( int place ){
        curr = head;
        T data = null;

        //if removing beginning of list, make temporary head point to head node's next and set head equal to the temporary node
        if (place <= 0){
            Node<T> tempHead = head.next;
            head = tempHead;
            this.size--;
            return head.data;
        }

        //create another Node called prevNode which will be used to 'cut off' the node to be removed
        Node<T> prevNode = null;
        int max = place - 1;
        if ( size - 1 <= place ){
            max = size - 1;
        }
        for (int i = 0; i < max; i++){
            prevNode = curr;
            curr = curr.next;
        }

        data = curr.data;
        prevNode.next = curr.next;
        this.size--;
        return data;

    }

    /**
     * Gets the data from a provided index (stating at index zero)
     * @param place the index to retreive data from
     * @return the data at index place
     * @throws ArrayIndexOutOfBoundsException if place is outside the list
     * time complexity: o(N)
     * space complexity: o(1)
     */
    public T get( int place )throws ArrayIndexOutOfBoundsException{
        curr = head;
        curr.next = head.next;
        // TODO: Complete Me
        if (place < 0 || place > size){
            throw new ArrayIndexOutOfBoundsException("ArrayIndexOutOfBoundsException");
        }
        for (int i = 0; i < place; i++){
            curr = curr.next;
        }
        return curr.data;
    }

    /**
     * Convert the LList into a String
     * @return a String in format [E0, E1, E2, ...]
     * space complexity: o(1)
     * time complexity: o(N)
     */
    public String toString(){
        //first part of string
        String tempString = "[";

        //capture all data
        for (int i = 0; i < size; i++){
            tempString += curr.data;
            curr = curr.next;
        }

        //last part of string
        tempString = tempString + "]";
        return tempString;
    }

    /**
     * Provides an iterator for the list
     * @return a new iterator starting at the first element of the list
     */
    @Override
    public Iterator<T> iterator() {
        return new LListIterator<T>();
    }

    /**
     * Prints the linked list to the console
     */
    public void print(){
        //TODO: Comment this before submission

		/*Node<T> current = this.head;
        int spot = 0;
        while( current != null ){
            System.out.println(spot+": " + current.data.toString());
            spot = spot + 1;
            current = current.next;
        }*/
    }

    /**
     * Nodes that specific to the linked list
     * @param <E> the type of the Node. It must by T or extend it.
     */
    private class Node<E extends T>{
        //data each Node holds
        private E data;

        //pointer to next node
        private Node<E> next;
        // TODO: Add appropriate attributes

        public Node( E data ){
            //first ensure valid data, then assign values to private attributes
            if (data != null) {
                this.data = data;
                this.next = null;
            }

        }
    }

    /**
     * The iterator that is used for our list.
     */
    private class LListIterator<E extends T> implements Iterator<E>{
        //pointer to current node
        private Node<T> current;


        public LListIterator(){
            //initialize iterator to point to beginning of list upon object creation
            current = head;

        }

        /**
         * see if there is a further node that does NOT point to null
         * @return true if there's a valid next node, else false
         */
        @Override
        public boolean hasNext() {

            return current != null;
            // TODO: Complete Me
        }

        /**
         * increment the pointer one node down the list if the current node has a next node
         * @return data of the node
         */
        @Override
        public E next() {
            if (hasNext()){
                //store data
                //cast data to E to match types (T and E WILL be of same type, so casting is okay)
                E data = (E)current.data;

                //increment current and return data
                current = current.next;
                return data;
            }
            // TODO: Complete Me
            return null;
        }
    }
}

package sample;

public class MusicQueue{

    private int capacity = 2;
    String queue[];
    int front = 0;
    int rear = -1;
    int currentSize = 0;

    public MusicQueue(){
        queue = new String[this.capacity];
    }

    /**
     * this method adds element at the end of the queue.
     * @param song_to_be_enqueued
     */
    public void enqueue(String song_to_be_enqueued) {
        if (isQueueFull()) {
            System.out.println("Queue is full, increase capacity...");
            increaseCapacity();
        }
        rear++;
        if(rear >= queue.length && currentSize != queue.length){
            rear = 0;
        }
        queue[rear] = song_to_be_enqueued;
        currentSize++;
        System.out.println("Adding: " + song_to_be_enqueued);
    }

    /**
     * this method removes an element from the top of the queue
     */
    public void dequeue() {
        if (isQueueEmpty()) {
            System.out.println("Underflow ! Unable to remove element from Queue");
        } else {
            front++;
            System.out.println("removed: " + queue[front-1]);
            queue[front-1]="";
            front = 0;
            currentSize--;
        }
        dequeue_adjust();
    }
    
    /**
     * this function readjusts the queue after dequeueing is complete
     */
    public void dequeue_adjust(){
        if (isQueueEmpty()) {
            System.out.println("Underflow ! Unable to remove element from Queue");
        } else {
            int counter=0;
            while(queue[counter+1]!=null)
            {
                queue[counter]=queue[counter+1];
                counter++;
            }
            queue[counter]=null;
        }
    }

    /**
     * this method adjust the queue on removal on dequeue
     *
    public void queue_adjust(){
        if (isQueueEmpty()) {
            System.out.println("Underflow ! Unable to remove element from Queue");

        } else{
            for(int index=0;index<queue.length;index++)
            {
                if(queue[index].equals("")||queue[index]==null)
                {
                    int limit=queue.length-(index+2);
                    int counter=0;
                    while(true)
                    {
                        if(queue[index+counter+1]==null)
                            break;
                        queue[index+counter]=queue[index+counter+1];
                        counter++;
                    }
                    queue[index+counter+1]=null;
                }
            }
        }
    }
    */

    /**
     * This method checks whether the queue is full or not
     * @return boolean
     */
    public boolean isQueueFull(){
        boolean status = false;
        if (currentSize == queue.length){
            status = true;
        }
        return status;
    }

    /**
     * This method checks whether the queue is empty or not
     * @return boolean
     */
    public boolean isQueueEmpty(){
        boolean status = false;
        if (currentSize == 0){
            status = true;
        }
        return status;
    }

    /**
     * Inner Function to increase the capacity of the queue
     */
    private void increaseCapacity(){
        //create new array with double size as the current one.
        int newCapacity = this.queue.length*2;
        String[] temperoryArray = new String[newCapacity];
        //copy elements to new array, copy from rear to front
        int tmpFront = front;
        int index = -1;
        while(true){
            temperoryArray[++index] = this.queue[tmpFront];
            tmpFront++;
            if(tmpFront == this.queue.length){
                tmpFront = 0;
            }
            if(currentSize == index+1){
                break;
            }
        }
        //make new array as queue
        this.queue = temperoryArray;
        System.out.println("New array capacity: "+this.queue.length);
        //reset front & rear values
        this.front = 0;
        this.rear = index;
    }

    /**
     * this function prints out the queue
     */
    public void show_queue() {
        for (int index = 0; index < queue.length; index++) {
            System.out.print(queue[index] + "   ");
            if (queue[index + 1] == null) {break;}
        }
        System.out.println();
    }

    /**
     * @param position Give the position as natural numbers position = 1,2,3,4......
     * @return String = the value of the block of queue at that position and if
     */
    public String in_queue_position(int position){
        if(position<= queue.length)
            return queue[position-1];
        else return "";
    }

    public static void main(String a[]){

        MusicQueue queue = new MusicQueue();
        queue.enqueue("S001");
        //queue.dequeue();
        queue.enqueue("S056");
        queue.enqueue("S002");
        queue.enqueue("S067");
        //queue.dequeue();
        queue.enqueue("S024");
        queue.enqueue("S098");
        //queue.dequeue();
        //queue.dequeue();
        //queue.dequeue();
        queue.enqueue("S435");
        //queue.dequeue();
        //queue.dequeue();
        queue.enqueue("S004");
        //queue.dequeue();
        queue.enqueue("S056");
        queue.enqueue("S002");
        queue.enqueue("S067");
        //queue.dequeue();
        queue.enqueue("S024");
        queue.enqueue("S098");
        //queue.dequeue();
        //queue.dequeue();
        //queue.dequeue();
        queue.enqueue("S435");
        queue.show_queue();
        System.out.println(queue.front);
        queue.dequeue();
        System.out.println(queue.in_queue_position(4));
        queue.show_queue();
        queue.dequeue();queue.dequeue();queue.dequeue();queue.dequeue();queue.dequeue();queue.dequeue();
        queue.show_queue();queue.dequeue();
        queue.show_queue();
    }
}
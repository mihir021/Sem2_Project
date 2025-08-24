package DS;
public class HistoryStack<T> {
    Node<T> top;
    int size;
    static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public HistoryStack() {
        top = null;
        size = 0;
    }
    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        if (top != null) {
            newNode.next = top;
        }
        top=newNode;
        size++;
    }
    public T pop() {
        if (isEmpty()) return null;

        T data = top.data;
        top = top.next;
        size--;
        return data;
    }
    public boolean isEmpty() {
        return top == null;
    }
}


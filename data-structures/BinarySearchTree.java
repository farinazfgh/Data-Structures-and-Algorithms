public class BinarySearchTree {

    static class Node {
        Node lchild;
        int data;
        Node rchild;
    }

    public BinarySearchTree() {
        this.root = null;
    }

    Node root;

    Node getRoot() {
        return root;
    }

    void Insert(int key) {

        Node t = root;
        Node p;
        Node r = null;

        // root is empty
        if (root == null) {
            p = new Node();
            p.data = key;
            p.lchild = null;
            p.rchild = null;
            root = p;
            return;
        }

        while (t != null) {
            r = t;
            if (key < t.data) {
                t = t.lchild;
            } else if (key > t.data) {
                t = t.rchild;
            } else {
                return;
            }
        }

        // Now t points at NULL and r points at insert location
        p = new Node();
        p.data = key;
        p.lchild = null;
        p.rchild = null;

        if (key < r.data) {
            r.lchild = p;
        } else {
            r.rchild = p;
        }

    }

    void Inorder(Node p) {
        if (p != null) {
            Inorder(p.lchild);
            System.out.println(p.data + ", ");
            Inorder(p.rchild);
        }
    }

    Node Search(int key) {
        Node t = root;
        while (t != null) {
            if (key == t.data) {
                return t;
            } else if (key < t.data) {
                t = t.lchild;
            } else {
                t = t.rchild;
            }
        }
        return null;
    }


    public static void main(String[] args) {

        BinarySearchTree bst = new BinarySearchTree();

        // Insert
        bst.Insert(10);
        bst.Insert(5);
        bst.Insert(20);
        bst.Insert(8);
        bst.Insert(30);

        // Inorder traversal
        bst.Inorder(bst.getRoot());
        System.out.println();

        // Search
        Node temp = bst.Search(2);
        if (temp != null) {
            System.out.println(+temp.data);
        } else {
            System.out.println("Element not found");
        }

    }
}

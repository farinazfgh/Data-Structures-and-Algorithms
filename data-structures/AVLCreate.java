public class AVLCreate {

    static class Node {
        Node lchild;
        int data;
        Node rchild;
        int height;
    }

    Node root;

    AVLCreate() {
        root = null;
    }

    void Inorder() {
        Inorder(root);
    }

    Node getRoot() {
        return root;
    }

    int getnodeHeight(Node node) {
        int hl;
        int hr;

        if (node != null && node.lchild != null)
            hl = node.lchild.height;
        else hl = 0;
        if (node != null && node.rchild != null)
            hr = node.rchild.height;
        else hr = 0;

        if (hl > hr) return hl + 1;
        else return hr + 1;
    }

    int getBalanceFactor(Node node) {
        int hl;
        int hr;

        if (node != null && node.lchild != null) hl = node.lchild.height;
        else hl = 0;
        if (node != null && node.rchild != null) hr = node.rchild.height;
        else hr = 0;

        return (hl - hr);
    }

    Node llrotation(Node node) {
        Node pl = node.lchild;
        Node plr = pl.rchild;

        pl.rchild = node;
        node.lchild = plr;

        // Update height
        node.height = getnodeHeight(node);
        pl.height = getnodeHeight(pl);

        // Update root
        if (root == node) {
            root = pl;
        }
        return pl;
    }

    Node RRRotation(Node node) {
        Node pr = node.rchild;
        Node prl = pr.lchild;

        pr.lchild = node;
        node.rchild = prl;

        // Update height
        node.height = getnodeHeight(node);
        pr.height = getnodeHeight(pr);

        // Update root
        if (root == node) {
            root = pr;
        }
        return pr;
    }

    Node LRRotation(Node node) {
        Node pl = node.lchild;
        Node plr = pl.rchild;

        pl.rchild = plr.lchild;
        node.lchild = plr.rchild;

        plr.lchild = pl;
        plr.rchild = node;

        // Update height
        pl.height = getnodeHeight(pl);
        node.height = getnodeHeight(node);
        plr.height = getnodeHeight(plr);

        // Update root
        if (node == root) {
            root = plr;
        }
        return plr;
    }

    Node RLRotation(Node node) {
        Node pr = node.rchild;
        Node prl = pr.lchild;

        pr.lchild = prl.rchild;
        node.rchild = prl.lchild;

        prl.rchild = pr;
        prl.lchild = node;

        // Update height
        pr.height = getnodeHeight(pr);
        node.height = getnodeHeight(node);
        prl.height = getnodeHeight(prl);

        // Update root
        if (root == node) {
            root = prl;
        }
        return prl;
    }


    void Inorder(Node node) {
        if (node != null) {
            Inorder(node.lchild);
            System.out.println(+node.data + ", ");
            Inorder(node.rchild);
        }
    }

    Node InPre(Node node) {
        while (node != null && node.rchild != null) {
            node = node.rchild;
        }
        return node;
    }

    Node InSucc(Node node) {
        while (node != null && node.lchild != null) {
            node = node.lchild;
        }
        return node;
    }

    Node insert(Node node, int key) {
        Node t;
        if (node == null) {
            t = new Node();
            t.data = key;
            t.lchild = null;
            t.rchild = null;
            t.height = 1;  // Starting height from 1 onwards instead of 0
            return t;
        }

        if (key < node.data) {
            node.lchild = insert(node.lchild, key);
        } else if (key > node.data) {
            node.rchild = insert(node.rchild, key);
        }

        // Update height
        node.height = getnodeHeight(node);

        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor == 2) {
            if (getBalanceFactor(node.lchild) == 1) return llrotation(node);
            if (getBalanceFactor(node.lchild) == -1) return LRRotation(node);
        }
        if (balanceFactor == -2) {
            if (getBalanceFactor(node.rchild) == -1) return RRRotation(node);
            if (getBalanceFactor(node.rchild) == 1) return RLRotation(node);
        }
        return node;
    }

    Node Delete(Node node, int key) {
        if (node == null) {
            return null;
        }

        if (node.lchild == null && node.rchild == null) {
            if (node == root) {
                root = null;
            }
            return null;
        }

        if (key < node.data) {
            node.lchild = Delete(node.lchild, key);
        } else if (key > node.data) {
            node.rchild = Delete(node.rchild, key);
        } else {
            Node q;
            if (getnodeHeight(node.lchild) > getnodeHeight(node.rchild)) {
                q = InPre(node.lchild);
                node.data = q.data;
                node.lchild = Delete(node.lchild, q.data);
            } else {
                q = InSucc(node.rchild);
                node.data = q.data;
                node.rchild = Delete(node.rchild, q.data);
            }
        }

        // Update height
        node.height = getnodeHeight(node);

        // Balance Factor and Rotation
        if (getBalanceFactor(node) == 2 && getBalanceFactor(node.lchild) == 1) {  // L1 Rotation
            return llrotation(node);
        } else if (getBalanceFactor(node) == 2 && getBalanceFactor(node.lchild) == -1) {  // L-1 Rotation
            return LRRotation(node);
        } else if (getBalanceFactor(node) == -2 && getBalanceFactor(node.rchild) == -1) {  // R-1 Rotation
            return RRRotation(node);
        } else if (getBalanceFactor(node) == -2 && getBalanceFactor(node.rchild) == 1) {  // R1 Rotation
            return RLRotation(node);
        } else if (getBalanceFactor(node) == 2 && getBalanceFactor(node.lchild) == 0) {  // L0 Rotation
            return llrotation(node);
        } else if (getBalanceFactor(node) == -2 && getBalanceFactor(node.rchild) == 0) {  // R0 Rotation
            return RRRotation(node);
        }

        return node;
    }

    public static void main(String[] args) {
        System.out.println();
        // LR Rotation
        AVLCreate tlr = new AVLCreate();
        tlr.root = tlr.insert(tlr.root, 10);
        tlr.root = tlr.insert(tlr.root, 20);
        tlr.root = tlr.insert(tlr.root, 30);
        tlr.root = tlr.insert(tlr.root, 25);
        tlr.root = tlr.insert(tlr.root, 28);
        tlr.root = tlr.insert(tlr.root, 27);
        tlr.root = tlr.insert(tlr.root, 5);


        tlr.Inorder();
        System.out.println();
        System.out.println(+tlr.root.data);

        // RL Rotation
        AVLCreate trl = new AVLCreate();
        trl.root = trl.insert(trl.root, 20);
        trl.root = trl.insert(trl.root, 50);
        trl.root = trl.insert(trl.root, 30);


        trl.Inorder();
        System.out.println();
        System.out.println(+trl.root.data);
    }
}

package application;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


/**
 * Implementation of a B+ tree to allow efficient access to many different indexes of a large data
 * set. BPTree objects are created for each type of index needed by the program. BPTrees provide an
 * efficient range search as compared to other types of data structures due to the ability to
 * perform log_m N lookups and linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;

    // Branching factor is the number of children nodes
    // for internal nodes of the tree
    private int branchingFactor;


    /**
     * Public constructor
     * 
     * @param branchingFactor
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
        }
        // TODO : Complete
        this.root = new LeafNode();
        this.branchingFactor = branchingFactor;
    }


    /*
     * (non-Javadoc)
     * 
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    @Override
    public void insert(K key, V value) {
        // TODO : Complete
        root.insert(key, value);
        // System.out.println(root.keys);
        InternalNode result = (InternalNode) root.split();

        if (result != null) {
            // The old root was split into two parts.
            // We have to create a new root pointing to them
            InternalNode _root = new InternalNode();
            _root.keys = result.keys;
            _root.children = result.children;
            root = _root;
        }
        // System.out.println(root.keys);
    }


    /*
     * (non-Javadoc)
     * 
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && !comparator.contentEquals("==")
                        && !comparator.contentEquals("<=")) {
            return new ArrayList<V>();
        }
        else {
            return root.rangeSearch(key, comparator);
        }

    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }


    /**
     * This abstract class represents any type of node in the tree This class is a super class of
     * the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {

        // List of keys
        List<K> keys;

        /**
         * Package constructor
         */
        Node() {
            // TODO : Complete
            keys = new ArrayList<K>();
        }

        /**
         * Inserts key and value in the appropriate leaf node and balances the tree if required by
         * splitting
         * 
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();

        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();

        /*
         * (non-Javadoc)
         * 
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();

        abstract boolean isLeafNode();

        public String toString() {
            return keys.toString();
        }

    } // End of abstract class Node

    /**
     * This class represents an internal node of the tree. This class is a concrete sub class of the
     * abstract Node class and provides implementation of the operations required for internal
     * (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;

        /**
         * Package constructor
         */
        InternalNode() {
            super();
            // TODO : Complete
            children = new ArrayList<Node>();
        }

        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            // TODO : Complete
            return children.get(0).getFirstLeafKey();
        }

        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            // TODO : Complete
            return (keys.size() >= branchingFactor);
        }

        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
            // TODO : Complete

            if (children != null) {
                // System.out.println(children.size() + " size");
                // System.out.println(children.get(0).getFirstLeafKey());
                // System.out.println(children.get(1).getFirstLeafKey());
                
                int position = keys.size();
                for (int k = 0; k < keys.size(); k++) {
                        if (key.compareTo(keys.get(k)) <= 0) {
                            position = k;
                            break;
                    }
                }
//                System.out.println(position);
//                System.out.println(children.size());
//                System.out.println(children.get(position));
                children.get(position).insert(key, value);
                InternalNode newNode = (InternalNode) children.get(position).split();
              if (newNode != null) {
                  children.remove(position);
                  children.addAll(position, newNode.children);

                  int childPosition = keys.size();
                  for (int j = 0; j < keys.size(); j++) {
                      if (keys.get(j).compareTo(newNode.keys.get(0)) >= 0) {
                          childPosition = j;
                      }
                  }
                  if (keys.size() == childPosition) {
                      keys.add(newNode.keys.get(0));
                  }else {
                  keys.add(childPosition, newNode.keys.get(0));
              }
                
              }

//                if (key.compareTo(children.get(children.size() - 1).getFirstLeafKey()) > 0) {
//                    children.get(children.size() - 1).insert(key, value);
//                    InternalNode newNode = (InternalNode) children.get(children.size() - 1).split();
//                    if (newNode != null) {
//                        children.remove(children.size() - 1);
//                        System.out.println(children + " children");
//                        children.addAll(newNode.children);
//                        System.out.println(children + " children");
//                        int position1 = keys.size();
//
//                        keys.add(position1, newNode.keys.get(0));
//                        System.out.println(keys + " key");
//
//
//                    }
//                } else {
//                    for (int i = 0; i < children.size(); i++) {
//
//
//                        // System.out.println(children.size() + " size");
//                        if (key.compareTo(children.get(i).getFirstLeafKey()) <= 0) {
//                            if (i == 0) {
//                                children.get(i).insert(key, value);
//                                InternalNode newNode = (InternalNode) children.get(i).split();
//
//                                if (newNode != null) {
//                                    children.remove(i);
//                                    children.addAll(i, newNode.children);
//
//
//                                    int position = keys.size();
//                                    for (int j = 0; j < keys.size(); j++) {
//                                        if (keys.get(j).compareTo(newNode.keys.get(0)) > 0) {
//                                            position = j;
//                                            break;
//                                        }
//                                    }
//                                    keys.add(position, newNode.keys.get(0));
//                                    break;
//                                } else {
//                                    break;
//                                }
//                            } else {
//
//                                children.get(i - 1).insert(key, value);
//                                InternalNode newNode = (InternalNode) children.get(i - 1).split();
//                                if (newNode != null) {
//                                    children.remove(i - 1);
//                                    children.addAll(i - 1, newNode.children);
//
//                                    int position = keys.size();
//                                    for (int j = 0; j < keys.size(); j++) {
//                                        if (keys.get(j).compareTo(newNode.keys.get(0)) > 0) {
//                                            position = j;
//                                        }
//                                    }
//
//                                    keys.add(position, newNode.keys.get(0));
//                                    break;
//                                } else {
//                                    break;
//                                }
//
//                            }
//
//                        }
//                    }
//                }
            }


        }

        boolean isLeafNode() {
            return false;
        }

        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#split()
         */
        Node split() {
            if (isOverflow()) {
                int mid = 0;
                if (branchingFactor%2 == 0) {
                    mid = branchingFactor / 2 + 1;
                }else {
                    mid = (branchingFactor + 1) / 2;
                }

                InternalNode newNode = new InternalNode();
                InternalNode sibling = new InternalNode();

                // System.out.println(mid-1);
                newNode.keys.add(keys.get(mid - 1));
                sibling.keys.addAll(keys.subList(0, mid - 1));


                sibling.children.addAll(children.subList(0, mid));
                
                for (int i = 0; i < mid; i++) {
                    this.keys.remove(0);
                    this.children.remove(0);
                }
//                this.keys.removeAll(keys.subList(0, mid)); // BPTree
//                this.children.removeAll(children.subList(0, mid));


                newNode.children.add(sibling);
                newNode.children.add(this);

                return newNode;
            } else {
                return null;
            }
        }

        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            
            int position = keys.size();
            
            for (int k = 0; k < keys.size(); k++) {
                    if (key.compareTo(keys.get(k)) <= 0) {
                        position = k;
                        break;
                }
                    
            }
            
            return children.get(position).rangeSearch(key, comparator);
        }

    } // End of class InternalNode


    /**
     * This class represents a leaf node of the tree. This class is a concrete sub class of the
     * abstract Node class and provides implementation of the operations that required for leaf
     * nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {

        // List of values
        List<V> values;

        // Reference to the next leaf node
        LeafNode next;

        // Reference to the previous leaf node
        LeafNode previous;

        /**
         * Package constructor
         */
        LeafNode() {
            super();
            // TODO : Complete
            values = new ArrayList<V>();
            next = null;
            previous = null;
        }


        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            // TODO : Complete
            return keys.get(0);
        }

        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            // TODO : Complete
            return (keys.size() >= branchingFactor);
        }

        boolean isLeafNode() {
            return true;
        }

        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
            // TODO : Complete
            int position = keys.size();
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i).compareTo(key) > 0) {
                    position = i;
                    break;
                }else if (keys.get(i).compareTo(key) == 0){
                    position = i;
                    break;
                }
            }
            if (keys.size() == position) {
                keys.add(key);
                values.add(value);
            }else {
            keys.add(position, key);
            values.add(position, value);
            }

//          int position = keys.size();
//          for (int i = keys.size()-1; i >= 0; i--) {
//              if (keys.get(i).compareTo(key) <= 0) {
//                  position = i+1;
//                  break;
//              }
//          }
//          if (keys.size() == position) {
//              keys.add(key);
//              values.add(value);
//          }else {
//              keys.add(position, key);
//              values.add(position, value);
//          }

        }

        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#split()
         */
        Node split() {
            // TODO : Complete
            if (isOverflow()) {
                int mid = 0;
                if (branchingFactor%2 == 0) {
                    mid = branchingFactor / 2 + 1;
                }else {
                    mid = (branchingFactor + 1) / 2;
                }

                InternalNode newNode = new InternalNode();
                LeafNode sibling = new LeafNode();

                // System.out.println(mid-1);
                newNode.keys.add(keys.get(mid - 1));
                // System.out.println(newNode.keys);

                sibling.keys.addAll(keys.subList(0, mid - 1));
                for (int i = 0; i < mid-1; i++) {
                    this.keys.remove(0);
                }
//                this.keys.removeAll(keys.subList(0, mid - 1)); // BPTree

                sibling.values.addAll(values.subList(0, mid - 1));
                
                for (int i = 0; i < mid-1; i++) {
                    this.values.remove(0);
                }
//                this.values.removeAll(sibling.values);

                newNode.children.add(sibling);
                newNode.children.add(this);
                sibling.previous = this.previous;
                if (this.previous != null) {
                    this.previous.next = sibling;
                }
                this.previous = sibling;
                sibling.next = this;

                
                // System.out.println(newNode.keys);
                // System.out.println(newNode.children.size());
                return newNode;
            } else {
                return null;
            }
        }

        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            
            ArrayList<V> returnList = new ArrayList<V>();
            
            if (comparator.contentEquals(">=")) {
             // System.out.println(keys + "keys");
                for (int i = 0; i < keys.size(); i++) {
                    if (keys.get(i).compareTo(key) >= 0) {
                        returnList.add(values.get(i));
//                        System.out.println(returnList);
                    }
                }  
                
                
                LeafNode current = this;
                LeafNode checkPrevious = this;
                while (current.next != null) {
                    for (int j = 0; j < current.next.keys.size(); j++) {
                        if (current.next.keys.get(j).compareTo(key) >= 0) {
                            returnList.add(current.next.values.get(j));
                        }
                    }

 //                   System.out.println(current.next.keys + "next");
//                    System.out.println(returnList);
                    current = current.next;
                }
                
                while (checkPrevious.previous != null) {
                    for (int j = checkPrevious.previous.keys.size() - 1; j >= 0; j--) {
                        if (checkPrevious.previous.keys.get(j).compareTo(key) >= 0) {
                            returnList.add(0, checkPrevious.previous.values.get(j));
                        }
                    }
                    checkPrevious = checkPrevious.previous;
                }
                
                
            }else if (comparator.contentEquals("==")) {
             
                if (keys.contains(key)) {
                    for (int i = 0; i < keys.size(); i++) {
                        if (keys.get(i).equals(key)) {
                            returnList.add(values.get(i));
                        }
                    }              
                }
                
                LeafNode current = this;
                LeafNode checkPrevious = this;
                while (current.next != null) {
                    for (int j = 0; j < current.next.keys.size(); j++) {
                        if (current.next.keys.get(j).compareTo(key) == 0) {
                            returnList.add(current.next.values.get(j));
                        }
                    }

 //                   System.out.println(current.next.keys + "next");
//                    System.out.println(returnList);
                    current = current.next;
                }
                
                while (checkPrevious.previous != null) {
                    for (int j = checkPrevious.previous.keys.size() - 1; j >= 0; j--) {
                        if (checkPrevious.previous.keys.get(j).compareTo(key) == 0) {
                            returnList.add(0, checkPrevious.previous.values.get(j));
                        }
                    }
                    checkPrevious = checkPrevious.previous;
                }
                
                
            }else if (comparator.contentEquals("<=")){
                
                for (int i = 0; i < keys.size(); i++) {
                    if (keys.get(i).compareTo(key) <= 0) {
                        returnList.add(values.get(i));
                    }
                }  
                
                LeafNode current = this;
                LeafNode checkPrevious = this;
                while (current.next != null) {
                    for (int j = 0; j < current.next.keys.size(); j++) {
                        if (current.next.keys.get(j).compareTo(key) <= 0) {
                            returnList.add(current.next.values.get(j));
                        }
                    }
                    current = current.next;
                }
                
                while (checkPrevious.previous != null) {
                    for (int j = checkPrevious.previous.keys.size() - 1; j >= 0; j--) {
                        if (checkPrevious.previous.keys.get(j).compareTo(key) <= 0) {
                            returnList.add(0, checkPrevious.previous.values.get(j));
                        }
                    }
                    checkPrevious = checkPrevious.previous;
                }
            }
            // TODO : Complete
            return returnList;
        }

    } // End of class LeafNode


    /**
     * Contains a basic test scenario for a BPTree instance. It shows a simple example of the use of
     * this class and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);
        Random rnd1 = new Random();
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Double j = dd[rnd1.nextInt(4)];
            list.add(j);
            bpTree.insert(j, j);
            System.out.println(j);
            System.out.println("\n\nTree structure:\n" + bpTree.toString());
        }
        List<Double> filteredValues = bpTree.rangeSearch(0.2d, ">=");
        System.out.println("Filtered values: " + filteredValues.toString());
        
//        BPTree<Integer, Integer> bpTree = new BPTree<>(4);
//        List<Integer> list = new ArrayList<>();
//        for (int i = 1; i < 6; i++) {
//
//            bpTree.insert(i, i);
//            System.out.println("\n\nTree structure:\n" + bpTree.toString());
//        }
//      List<Integer> filteredValues = bpTree.rangeSearch(15, ">=");
//
//      System.out.println("Filtered values: " + filteredValues.toString());
//        
        
//        bpTree.insert(1, 1);
//        System.out.println("\n\nTree structure:\n" + bpTree.toString());
//        bpTree.insert(3, 1);
//        System.out.println("\n\nTree structure:\n" + bpTree.toString());
//        bpTree.insert(5, 1);
//        System.out.println("\n\nTree structure:\n" + bpTree.toString());
//        bpTree.insert(2, 1);
//        System.out.println("\n\nTree structure:\n" + bpTree.toString());
//        bpTree.insert(4, 1);
//        System.out.println("\n\nTree structure:\n" + bpTree.toString());
//        bpTree.insert(0, 1);
//        System.out.println("\n\nTree structure:\n" + bpTree.toString());
//        bpTree.insert(6, 1);
//        System.out.println("\n\nTree structure:\n" + bpTree.toString());
        // List<Double> filteredValues = bpTree.rangeSearch(0.2d, ">=");
        // System.out.println("Filtered values: " + filteredValues.toString());
    }

} // End of class BPTree

package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.Queue;

/*Data Structures:

Arrays:
- Read: O(1)
- Insertion: O(n)
- Deletion: O(n)
- Fast at reading but slow at insertion and deletion.

Linked Lists:
- Read: O(n)
- Insertion: O(1)
- Deletion: O(1)
- Slow at reading but efficient for insertion and deletion.

HashMaps:
- Read: O(1)
- Insertion: O(1)
- Deletion: O(1)
- Similar to arrays but with named indexes (keys); unordered but provide fast lookup.

Stacks:
- Push: O(1)
- Pop: O(1)
- Peak: O(1)
- Follow the LIFO (Last In, First Out) principle; useful for fast retrieval of the topmost element but can be cumbersome for inserting or deleting elements in the middle or end.

Queues:
- Enqueue: O(1)
- Dequeue: O(1)
- Front: O(1)
- Follow the FIFO (First In, First Out) principle; the first element in line is the first to come out. Think of them as playlists for organizing items in order of arrival.

Trees:
- Read/Search: O(log n)
- Insertion: O(log n)
- Deletion: O(log n)
- Nodes connected by edges; root, parent-child connections.

Binary Trees:
- Efficient searching of ordered values.
- Follow a binary search property where left child nodes are less than the parent and right child nodes are greater.
- Useful for tasks like number guessing games or dictionary implementations.

Graphs:
- Traversal/Search: O(V + E) (V: number of vertices, E: number of edges)
- Insertion: O(1)
- Deletion: O(1)
- Versatile models for connections between nodes and edges; can be directed or undirected with no neighboring limit. Can include cycles and weights on paths. Used for tasks like route optimization.*/

public class CollectionsDemo {

    public static void main(String[] args) {
        System.out.println("--- Java Collections Demonstration ---");

        // 1. List: Ordered collection, allows duplicates.
        demoList();

        // 2. Set: Unordered collection, stores unique elements.
        demoSet();

        // 3. Map: Stores key-value pairs, unique keys.
        demoMap();

        // 4. Queue: Ordered for processing (FIFO by default).
        demoQueue();
    }

    /**
     * Demonstrates a List using ArrayList.
     */
    public static void demoList() {
        System.out.println("\n--- List (ArrayList) ---");
        List<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Apple"); // Duplicates allowed in List
        System.out.println("List elements: " + list);
        System.out.println("Element at index 1: " + list.get(1));
        list.remove("Apple"); // Removes the first occurrence
        System.out.println("List after removing 'Apple': " + list);
    }

    /**
     * Demonstrates a Set using HashSet.
     */
    public static void demoSet() {
        System.out.println("\n--- Set (HashSet) ---");
        Set<String> set = new HashSet<>();
        set.add("Dog");
        set.add("Cat");
        set.add("Dog"); // Duplicate "Dog" is ignored
        System.out.println("Set elements: " + set);
        System.out.println("Does Set contain 'Cat'? " + set.contains("Cat"));
        set.remove("Dog");
        System.out.println("Set after removing 'Dog': " + set);
    }

    /**
     * Demonstrates a Map using HashMap.
     */
    public static void demoMap() {
        System.out.println("\n--- Map (HashMap) ---");
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "One");
        map.put(2, "Two");
        map.put(3, "Three");
        map.put(2, "UpdatedTwo"); // Updates the value for key 2
        System.out.println("Map entries: " + map);
        System.out.println("Value for key 1: " + map.get(1));

        // Iterating through a Map's key-value pairs
        System.out.print("Iterating Map: ");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.print(entry.getKey() + ":" + entry.getValue() + " ");
        }
        System.out.println();
    }

    /**
     * Demonstrates a Queue using PriorityQueue.
     */
    public static void demoQueue() {
        System.out.println("\n--- Queue (PriorityQueue) ---");
        Queue<Integer> queue = new PriorityQueue<>();
        queue.add(30);
        queue.add(10);
        queue.add(20);
        System.out.println("Queue elements (insertion order may vary due to priority): " + queue);
        System.out.println("Head of the queue (peek): " + queue.peek()); // Element with highest priority (lowest value here)
        System.out.println("Remove head (poll): " + queue.poll()); // Removes and returns head
        System.out.println("Queue after poll: " + queue);
    }
}

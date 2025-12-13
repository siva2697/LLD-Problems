
---

## 📝 Inventory Management LLD Problem Statement

The goal is to design a highly concurrent, thread-safe, and scalable `InventoryManager` that supports the complete e-commerce transaction lifecycle, minimizing the time items spend reserved unnecessarily.

### Core Challenge: Atomicity, Granularity, and Scalability

The primary challenge is designing the system to handle thousands of concurrent reservation requests while maintaining **stock integrity** and **high throughput**.

### Key Problems to Address in the LLD:

#### 1. Concurrency and Deadlock Avoidance

The system must ensure that simultaneous operations on inventory do not lead to corruption or deadlock.

* **Problem:** Using a single lock (e.g., `synchronized` method) bottlenecks the system. Using multiple item-specific locks (`itemLocks`) introduces the risk of **deadlock** if threads acquire locks in different orders (e.g., Thread 1 locks $A$ then $B$; Thread 2 locks $B$ then $A$).
* **LLD Requirement:** Define a mechanism (like **canonical lock ordering**) to guarantee that a thread attempting to reserve multiple items always acquires their respective `itemLocks` in a deterministic sequence (e.g., sorted by item ID).

#### 2. Transaction Integrity and Rollback

A multi-item reservation must be atomic: all items must be reserved, or none must be.

* **Problem:** If a thread successfully reserves items 1, 2, and 3, but item 4 is out of stock, the transaction must fail, and the inventory for items 1, 2, and 3 must be **immediately rolled back** to the available stock pool.
* **LLD Requirement:** Design a pattern (e.g., a `try-catch-rollback` block within the critical section) that ensures **immediate compensation** for partial updates when a reservation fails mid-process.

#### 3. Lock Life Cycle and Memory Management

Locks used for synchronization must be managed to prevent memory leaks and concurrency errors.

* **Problem:** The `cartLocks` map, designed to synchronize multi-item operations for a single cart, grows indefinitely unless inactive locks are removed. However, removing a lock key (`cartLocks.remove(cartId)`) while another thread is waiting to create a new lock can break mutual exclusion (the race condition we discussed).
* **LLD Requirement:** Implement a strategy to safely clean up stale `cartLocks`. The LLD must justify the chosen solution:
    * **Option A (Safer):** Implement a **time-based scheduler** that removes locks only after a very long expiry period (e.g., 24 hours), betting on low memory overhead and high safety.
    * **Option B (More Complex):** Use `ConcurrentHashMap.remove(key, value)` to atomically remove the lock key only if the value matches the lock object currently held by the exiting thread.

#### 4. System Decoupling (Separation of Concerns)

The inventory system must provide a clean API to the higher-level order management service.

* **Problem:** The `OrderManager` should focus on business flow (Payment $\rightarrow$ Fulfillment) and should not be concerned with internal concurrency mechanisms (locks, rollbacks).
* **LLD Requirement:** Clearly define the external API (`reserveInventory`, `confirmReservation`, `releaseInventory/cancelReservation`) and ensure the `InventoryManager` fully encapsulates all concurrent logic, making it a reliable black box for stock state.
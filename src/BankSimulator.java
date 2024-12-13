import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

    class Account {
        private final String id;
        private double balance;
        private final ReentrantLock lock = new ReentrantLock();

        public Account(String id, double initialBalance) {
            this.id = id;
            this.balance = initialBalance;
        }

        public String getId() {
            return id;
        }

        public double getBalance() {
            return balance;
        }

        public void transfer(Account to, double amount) {
            lock.lock();
            try {
                if (balance >= amount) {
                    balance -= amount;
                    to.deposit(amount);
                } else {
                    System.err.println("Недостаточно средств на счете " + id);
                }
            } finally {
                lock.unlock();
            }
        }

        private void deposit(double amount) {
            lock.lock();
            try {
                balance += amount;
            } finally {
                lock.unlock();
            }
        }
    }

    public class BankSimulator {
        public static void main(String[] args) throws InterruptedException {
            Map<String, Account> accounts = new HashMap<>();
            accounts.put("A123", new Account("A123", 1000));
            accounts.put("B456", new Account("B456", 500));
            accounts.put("C789", new Account("C789", 2000));

            int numThreads = 3;
            Thread[] threads = new Thread[numThreads];
            Random random = new Random();

            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 10; j++) {
                        String fromId = getRandomAccountId(accounts);
                        String toId = getRandomAccountId(accounts);
                        double amount = random.nextDouble() * 100;
                        if (!fromId.equals(toId)){
                            accounts.get(fromId).transfer(accounts.get(toId), amount);
                        }
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
            accounts.forEach((id, acc) -> System.out.println("Аккаунт " + id + ": " + acc.getBalance()));
        }

        private static String getRandomAccountId(Map<String, Account> accounts) {
            int index = new Random().nextInt(accounts.size());
            return accounts.keySet().toArray(new String[0])[index];
        }
    }


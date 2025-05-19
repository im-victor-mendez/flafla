package com.example.flafla.utils.dev;

import com.example.flafla.enums.OrderStatus;
import com.example.flafla.models.AvailableItem;
import com.example.flafla.models.Cart;
import com.example.flafla.models.Order;
import com.example.flafla.models.OrderItem;
import com.example.flafla.models.ProductPromotion;
import com.example.flafla.models.PromotionCode;
import com.example.flafla.models.Review;
import com.example.flafla.models.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FirestoreDevSeeder {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void seedAll(Runnable onComplete) {
        seedUsers(onComplete);
        seedReviewsToSomeProducts(onComplete);
        seedProductPromotions(onComplete);
        seedPromotionCodes(onComplete);
        seedAvailableItems(onComplete);
    }

    public void seedUsers(Runnable onComplete) {
        CollectionReference usersRef = db.collection("users");

        List<User> users = new ArrayList<>();

        // Ejemplo: creamos 3 usuarios de prueba
        for (int i = 1; i <= 3; i++) {
            Cart cart = new Cart.Builder()
                    .setItems(Collections.emptyList()) // o agrega ProductItem si deseas
                    .build();

            users.add(new User.Builder()
                    .setId("user" + i)
                    .setName("Usuario " + i)
                    .setEmail("usuario" + i + "@correo.com")
                    .setPhone("555000000" + i)
                    .setCart(cart)
                    .build());
        }

        AtomicInteger counter = new AtomicInteger(users.size());

        for (User user : users) {
            usersRef.document(user.getId())
                    .set(user)
                    .addOnCompleteListener(task -> {
                        if (counter.decrementAndGet() == 0) {
                            onComplete.run();
                        }
                    });
        }
    }

    public void seedReviewsToSomeProducts(Runnable onComplete) {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String productId = "product" + i;

            // Solo agrega reviews a algunos productos (ej. probabilidad 60%)
            if (random.nextBoolean() || random.nextInt(3) == 0) {
                int numberOfReviews = 1 + random.nextInt(4); // 1 a 4 reviews

                for (int j = 0; j < numberOfReviews; j++) {
                    Review review = new Review.Builder()
                            .setAuthor_id("user" + random.nextInt(10)) // author_id: user0 - user9
                            .setCreated_at(new Date(System.currentTimeMillis() - random.nextInt(1000000000)))
                            .setContent("Reseña aleatoria #" + (j + 1) + " para " + productId)
                            .setRating(1 + random.nextInt(4)) // 0 a 4
                            .build();

                    db.collection("products")
                            .document(productId)
                            .collection("reviews")
                            .add(review)
                            .addOnCompleteListener(docRef -> {
                                onComplete.run();
                            });
                }
            }
        }
    }

    public void seedProductPromotions(Runnable onComplete) {
        CollectionReference promotionsRef = db.collection("store")
                .document("promotions")
                .collection("products");

        List<ProductPromotion> promotions = new ArrayList<>();

        // Ejemplo: agregamos promociones tipo DISCOUNT para cada producto
        for (int i = 1; i <= 5; i++) {
            long millisIn7Days = 7L * 24 * 60 * 60 * 1000; // Milisegundos en 7 días
            long nowMillis = System.currentTimeMillis();
            long futureMillis = nowMillis + millisIn7Days;

            Timestamp validTo = new Timestamp(futureMillis / 1000, 0); // segundos y nanosegundos

            promotions.add(new ProductPromotion.Builder()
                    .setProductId("product" + i)
                    .setType(ProductPromotion.Type.DISCOUNT)
                    .setDiscountPercent(10 * i)  // ejemplo: 10%, 20%, ..., 50%
                    .setLabel("Promo " + (10 * i) + "%")
                    .setDescription("Descuento del " + (10 * i) + "% para product" + i)
                    .setValidFrom(Timestamp.now())
                    .setValidTo(validTo)
                    .build());
        }

        AtomicInteger counter = new AtomicInteger(promotions.size());

        for (ProductPromotion promo : promotions) {
            promotionsRef.document(promo.getProduct_id())
                    .set(promo)
                    .addOnCompleteListener(task -> {
                        if (counter.decrementAndGet() == 0) {
                            onComplete.run();
                        }
                    });
        }
    }

    public static void seedPromotionCodes(Runnable onComplete) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Códigos de promoción de ejemplo
        Timestamp now = Timestamp.now();
        Date nextWeekDate = new Date(now.toDate().getTime() + 7L * 24 * 60 * 60 * 1000);
        Timestamp nextWeek = new Timestamp(nextWeekDate);

        List<PromotionCode> codes = Arrays.asList(
                new PromotionCode.Builder()
                        .setCode("SPRING20")
                        .setDiscountPercent(20)
                        .setValidFrom(now)
                        .setValidTo(nextWeek)
                        .setActive(true)
                        .build(),
                new PromotionCode.Builder()
                        .setCode("WELCOME15")
                        .setDiscountPercent(15)
                        .setValidFrom(now)
                        .setValidTo(nextWeek)
                        .setActive(true)
                        .build(),
                new PromotionCode.Builder()
                        .setCode("SUMMER10")
                        .setDiscountPercent(10)
                        .setValidFrom(now)
                        .setValidTo(nextWeek)
                        .setActive(true)
                        .build()
        );

        // Guardar en store/promotions/codes
        Map<String, Object> data = new HashMap<>();
        data.put("codes", codes);

        db.collection("store")
                .document("promotions")
                .set(data, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    System.out.println("Promotion codes seeded successfully.");

                    onComplete.run();
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error seeding promotion codes: " + e.getMessage());
                });
    }

    public void seedAvailableItems(Runnable onComplete) {
        CollectionReference availableItemsRef = db.collection("store")
                .document("products")
                .collection("availableItems");

        List<AvailableItem> items = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            items.add(new AvailableItem.Builder()
                    .setProductId("product" + i)
                    .setQuantity(10)
                    .build());
        }

        AtomicInteger counter = new AtomicInteger(items.size());

        for (AvailableItem item : items) {
            // Usamos productId como documento para evitar duplicados
            availableItemsRef.document(item.getProductId())
                    .set(item)
                    .addOnCompleteListener(task -> {
                        if (counter.decrementAndGet() == 0) {
                            onComplete.run();
                        }
                    });
        }
    }

    public void seedRandomOrderToUser(String userId, int products, Runnable onComplete) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Mensajes de regalo aleatorios
        String[] giftMessages = {
                "¡Feliz cumpleaños!",
                "Con mucho cariño.",
                "Espero que te guste.",
                "Un detalle para ti.",
                "Gracias por todo."
        };

        // Crear items aleatorios
        List<OrderItem> randomItems = new ArrayList<>();
        Random random = new Random();
        int itemCount = random.nextInt(products) + 1;

        for (int i = 0; i < itemCount; i++) {
            String productId = "product" + String.valueOf(random.nextInt(5) + 1); // "1" a "5"
            int quantity = random.nextInt(3) + 1; // 1 a 3 unidades
            double price = 50 + (100 * random.nextDouble()); // $50 a $150

            OrderItem item = new OrderItem.Builder()
                    .setProductId(productId)
                    .setQuantity(quantity)
                    .setPrice(Math.round(price * 100.0) / 100.0)
                    .build();

            randomItems.add(item);
        }

        // Crear orden
        Order order = new Order.Builder()
                .setId(UUID.randomUUID().toString())
                .setItems(randomItems)
                .setDate(Timestamp.now())
                .setStatus(OrderStatus.values()[random.nextInt(OrderStatus.values().length)])
                .setGiftMessage(giftMessages[random.nextInt(giftMessages.length)])
                .build();

        // Guardar en Firestore
        db.collection("users")
                .document(userId)
                .collection("orders")
                .document(order.getId())
                .set(order)
                .addOnCompleteListener(runnable -> onComplete.run());
    }


}

package com.dias.exercise.firstlist;

import com.dias.exercise.firstlist.model.Customer;
import com.dias.exercise.firstlist.model.Order;
import com.dias.exercise.firstlist.model.Product;
import com.dias.exercise.firstlist.model.Subscription;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

public class Runner {

    public static void main(String[] args) throws IOException {
        Customer joao = new Customer("João Victor Dias");
        Customer grazielle = new Customer("Grazielle Rodrigues");
        Customer aurora = new Customer("Aurora Mary");

        Product metallica = new Product(
            "Metallica",
            Files.createTempFile("metallica", ".mp3").toAbsolutePath(),
            new BigDecimal("10.4"));

        Product epica = new Product(
            "Epica",
            Files.createTempFile("epica", ".mp3").toAbsolutePath(),
            new BigDecimal("48.30"));

        Product anita = new Product(
            "Anita",
            Files.createTempFile("anita", ".mp3").toAbsolutePath(),
            new BigDecimal("0.07"));

        /**
         * 1 Crie uma Classe com um método main para criar alguns produtos, clientes e pagamentos.
         *   Crie Pagamentos com:  a data de hoje, ontem e um do mês passado.
         */
        Order joaosOrder = new Order(joao, LocalDate.now(), Arrays.asList(metallica, epica));
        Order graziesOrder = new Order(grazielle, LocalDate.now().minusDays(1), Arrays.asList(epica, epica));
        Order aurorasOrder = new Order(aurora, LocalDate.now().minusMonths(1), Arrays.asList(metallica, metallica, anita));

        /**
         * 2 - Ordene e imprima os pagamentos pela data de compra.
         */
        List<Order> orders = Arrays.asList(joaosOrder, graziesOrder, aurorasOrder);
        orders.stream()
            // Ordenar ASC
            .sorted(Comparator.comparing(Order::getOrderDate))
            .forEach(System.out::println);

        /**
         * 3 - Calcule e Imprima a soma dos valores de um pagamento com optional e recebendo um Double diretamente.
         *
         * Optional nao foi necessario
         */
        orders.forEach(order -> {
                BigDecimal totalAmount = order
                    .getProducts()
                    .stream()
                    .map(Product::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                System.out.printf(
                    "Total amount for %s's Order: %s%n",
                    order.getCustomer().getName(),
                    totalAmount.setScale(2).toPlainString());
            });

        /**
         * 3 - Calcule e Imprima a soma dos valores de um pagamento com optional e recebendo um Double diretamente.
         *
         * Com double
         */

        orders.forEach(order -> {
            double totalAmount = 0d;
            for(Product product : order.getProducts()) {
                totalAmount += product.getPrice().setScale(2).doubleValue();
            }
            System.out.println(
                MessageFormat.format("Total amount for {0}''s Order: {1,number,#.00}", order.getCustomer().getName(), totalAmount));
        });

        /**
         * 4 -  Calcule o Valor de todos os pagamentos da Lista de pagamentos.
         */

        BigDecimal totalAmount = orders.stream()
            .map(Order::getProducts)
            .flatMap(List::stream)
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2);
        System.out.println("Total amount for all orders: " + totalAmount.toPlainString());

        /**
         * 5 - Imprima a quantidade de cada Produto vendido.
         */
        orders.stream()
            .map(Order::getProducts)
            .flatMap(List::stream)
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()))
            .forEach((key, value) -> System.out.printf("%s: %d%n", key, value));

        /**
         * 6 - Crie um Mapa de <Cliente, List<Produto> , onde Cliente pode ser o nome do cliente.
         */

        Map<String, List<Product>> productsByCustomer = orders.stream()
            .collect(Collectors.toMap(item -> item.getCustomer().getName(), Order::getProducts));

        Map<String, Double> totalAmountByCustomer = new HashMap<>();
        orders.forEach(order -> {
            BigDecimal amountByCustomer = order
                .getProducts()
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2);
            totalAmountByCustomer.put(order.getCustomer().getName(), amountByCustomer.doubleValue());
        });

        /**
         * 7 - Qual cliente gastou mais?
         */
        AtomicReference<String> greatestCustomer = new AtomicReference<>();
        productsByCustomer.forEach((key, value) -> {
            if(greatestCustomer.get() == null || greatestCustomer.get().isEmpty()){
                greatestCustomer.set(key);
            }else {
                greatestCustomer.set(
                    totalAmountByCustomer.get(key) > totalAmountByCustomer.get(greatestCustomer.get())
                        ? key
                        : greatestCustomer.get());
            }
        });
        System.out.printf("%s is the winner%n", greatestCustomer.get());


        /**
         * 8 - Quanto foi faturado em um determinado mês?
         */

        Map<YearMonth, Double> resultPerMonth = orders.stream()
            .collect(
                Collectors.groupingBy(order -> YearMonth.from(order.getOrderDate()),
                Collectors.summingDouble(order -> order
                    .getProducts()
                    .stream()
                    .map(Product::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2)
                    .doubleValue())));

        resultPerMonth.forEach((month, value) -> System.out.println(month.toString() + " - " + value));

        /**
         * 9 - Crie 3 assinaturas com assinaturas de 99.98 reais, sendo 2 deles com assinaturas encerradas.
         */

        Subscription joaosSubscription = new Subscription(
            new BigDecimal("99.98"),
            LocalDate.now().minusMonths(5),
            joao);

        Subscription joaosSubscription2 = new Subscription(
            new BigDecimal("99.98"),
            LocalDate.now().minusMonths(5),
            LocalDate.now().plusDays(32),
            joao);

        Subscription graziellesSubscription = new Subscription(
            new BigDecimal("99.98"),
            LocalDate.now().minusMonths(3),
            LocalDate.now(),
            grazielle);

        Subscription aurorassSubscription = new Subscription(
            new BigDecimal("99.98"),
            LocalDate.now().minusMonths(2),
            LocalDate.now(),
            aurora);

        List<Subscription> subscriptions = Arrays.asList(
            joaosSubscription,
            joaosSubscription2,
            graziellesSubscription,
            aurorassSubscription);

        subscriptions.stream()
            .filter(subscription -> subscription.getEnd() == null || subscription.getEnd().isAfter(LocalDate.now()))
            .forEach(subscription -> {
                if(subscription.getEnd() == null){
                    System.out.println("Subscription doesn't have an end day");
                }else {
                    System.out.printf("Remaining days: %d%n", DAYS.between(subscription.getBegin(), subscription.getEnd()));
                }
            });

        /**
         * 11 - Imprima o tempo de meses entre o start e end de todas assinaturas. Não utilize IFs para assinaturas sem end Time.
         */

        subscriptions.stream()
            .filter(subscription -> subscription.getEnd() != null)
            .forEach(subscription -> System.out.printf("Difference between start and end in days: %d%n", DAYS.between(subscription.getBegin(), subscription.getEnd())));


        /**
         * 12 - Calcule o valor pago em cada assinatura até o momento.
         */

        subscriptions
            .forEach(subscription -> {
                LocalDate virtualEndDate = Stream.of(subscription.getEnd(), LocalDate.now())
                    .filter(Objects::nonNull)
                    .min(LocalDate::compareTo)
                    .orElseThrow(() -> new RuntimeException("Invalid Input"));
                BigDecimal amountPaidSoFar = subscription.getMonthlyPrice().setScale(2)
                    .multiply(new BigDecimal(MONTHS.between(subscription.getBegin(), virtualEndDate) + 1));

                System.out.println(MessageFormat.format("{0} paid R${1,number,#.##}", subscription.getCustomer().getName(), amountPaidSoFar));
            });

    }
}

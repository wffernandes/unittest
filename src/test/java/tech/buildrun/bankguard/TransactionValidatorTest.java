package tech.buildrun.bankguard;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionValidatorTest {

    private final TransactionValidator validator = new TransactionValidator();

    @Nested
    class validateTransaction {

        @Test
        void shouldThrowIllegalArgumentWhenTransactionIsZero() {

            // ARRANGE
            var transaction = new Transaction(
                    0, "PIX", false, 0
            );

            // ACT & ASSERT
            assertThrows(IllegalArgumentException.class, () -> {
                validator.validateTransaction(transaction);
            });
        }

        @Test
        void shouldThrowIllegalArgumentWhenTransactionIsNegative() {

            // ARRANGE
            var transaction = new Transaction(
                    -2, "PIX", false, 0
            );

            // ACT & ASSERT
            assertThrows(IllegalArgumentException.class, () -> {
                validator.validateTransaction(transaction);
            });
        }

        @Test
        void shouldReturnBlockedWhenFailedAttemptsIsEqualTo3() {

            // ARRANGE
            var transaction = new Transaction(
                    20, "PIX", false, 3
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("BLOCKED", output);
        }

        @Test
        void shouldReturnBlockedWhenFailedAttemptsIsBiggerThan3() {

            // ARRANGE
            var transaction = new Transaction(
                    20, "PIX", false, 4
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("BLOCKED", output);
        }

        @Test
        void shouldReturnManualReviewWhenIsInternationalAndValueIsBiggerThan1000() {

            // ARRANGE
            var transaction = new Transaction(
                    1001, "PIX", true, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("MANUAL REVIEW", output);
        }

        @Test
        void shouldReturnManualReviewWhenIsNotInternationalAndValueIsBiggerThan1000() {

            // ARRANGE
            var transaction = new Transaction(
                    1001, "PIX", false, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("APPROVED", output);
        }

        @Test
        void shouldReturnApprovedWhenIsInternationalAndValueIsSmallerThan1000() {

            // ARRANGE
            var transaction = new Transaction(
                    0.5, "PIX", true, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("APPROVED", output);
        }

        @Test
        void shouldReturnApprovedWhenIsInternationalAndValueIsEqualTo1000() {

            // ARRANGE
            var transaction = new Transaction(
                    1000, "PIX", true, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("APPROVED", output);
        }

        @Test
        void shouldReturnManualReviewWhenIsPixAndIsBiggerThan5000() {

            // ARRANGE
            var transaction = new Transaction(
                    5001, "PIX", false, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("MANUAL REVIEW", output);
        }

        @Test
        void shouldReturnApprovedWhenIsPixAndIsEqualTo5000() {

            // ARRANGE
            var transaction = new Transaction(
                    5000, "PIX", false, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("APPROVED", output);
        }

        @Test
        void shouldReturnBlockedWhenIsBiggerThan10000() {

            // ARRANGE
            var transaction = new Transaction(
                    10001, "TED", false, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("BLOCKED", output);
        }

        @Test
        void shouldReturnApprovedWhenTEDAndIsEqualTo10000() {

            // ARRANGE
            var transaction = new Transaction(
                    10000, "TED", false, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("APPROVED", output);
        }

        @Test
        void shouldReturnApprovedWhenIsPixAndIsSmallerThan5000() {

            // ARRANGE
            var transaction = new Transaction(
                    3500, "PIX", false, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("APPROVED", output);
        }

        @Test
        void shouldReturnApprovedWhenIsPixAndIsEqualToCents() {

            // ARRANGE
            var transaction = new Transaction(
                    0.5, "PIX", false, 0
            );

            // ACT
            var output = validator.validateTransaction(transaction);

            // ASSERT
            assertEquals("APPROVED", output);
        }
    }

}
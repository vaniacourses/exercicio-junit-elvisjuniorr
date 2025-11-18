package test.java.com.example.jokenpo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import main.java.com.example.jokenpo.Main;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests para Main.jogar (Jokenpo)")
class MainTest {

    private final Main sut = new Main(); // System Under Test

    // Helper para reduzir repetição mantendo AAA claro
    private void assertJogarReturns(int jogador1, int jogador2, int expected) {
        // Arrange is trivial (sut already created)
        // Act
        int result = sut.jogar(jogador1, jogador2);
        // Assert
        assertEquals(expected, result,
                () -> String.format("Esperado %d para jogar(%d, %d) mas obteve %d",
                        expected, jogador1, jogador2, result));
    }

    @Nested
    @DisplayName("Casos de empate (happy path: iguais -> 0)")
    class Empates {

        @Test
        @DisplayName("1 vs 1 => empate")
        void empateWhenOneAndOne() {
            assertJogarReturns(1, 1, 0);
        }

        @Test
        @DisplayName("2 vs 2 => empate")
        void empateWhenTwoAndTwo() {
            assertJogarReturns(2, 2, 0);
        }

        @Test
        @DisplayName("3 vs 3 => empate")
        void empateWhenThreeAndThree() {
            assertJogarReturns(3, 3, 0);
        }
    }

    @Nested
    @DisplayName("Casos onde Jogador 1 vence (happy path)")
    class Jogador1Vence {

        @ParameterizedTest(name = "j1={0}, j2={1} -> esperado 1")
        @CsvSource({
                "1, 2", // papel vence pedra
                "2, 3", // pedra vence tesoura
                "3, 1"  // tesoura vence papel
        })
        void jogador1WinsAllValidCombos(int j1, int j2) {
            assertJogarReturns(j1, j2, 1);
        }
    }

    @Nested
    @DisplayName("Casos onde Jogador 2 vence (happy path)")
    class Jogador2Vence {

        @ParameterizedTest(name = "j1={0}, j2={1} -> esperado 2")
        @CsvSource({
                "2, 1", // pedra perde para papel
                "3, 2", // tesoura perde para pedra
                "1, 3"  // papel perde para tesoura
        })
        void jogador2WinsAllValidCombos(int j1, int j2) {
            assertJogarReturns(j1, j2, 2);
        }
    }

    @Nested
    @DisplayName("Entradas inválidas (qualquer jogador fora do intervalo 1..3) -> -1")
    class EntradasInvalidas {

        @ParameterizedTest(name = "j1 inválido {0} com j2 válido 1 -> -1")
        @ValueSource(ints = {-10, -1, 0, 4, 100})
        void jogador1InvalidoReturnsMinusOne(int invalidJ1) {
            assertJogarReturns(invalidJ1, 1, -1);
        }

        @ParameterizedTest(name = "j2 inválido {0} com j1 válido 1 -> -1")
        @ValueSource(ints = {-10, -1, 0, 4, 100})
        void jogador2InvalidoReturnsMinusOne(int invalidJ2) {
            assertJogarReturns(1, invalidJ2, -1);
        }

        @Test
        @DisplayName("Ambos inválidos -> -1")
        void bothInvalidReturnsMinusOne() {
            assertJogarReturns(0, 4, -1);
        }
    }

    @Nested
    @DisplayName("Valores de limite (boundary values) e casos extremos")
    class BoundaryValues {

        @Test
        @DisplayName("Limite inferior 0 (inválido) e limite superior 4 (inválido) - ambos fora -> -1")
        void limitsOutOfRange() {
            assertJogarReturns(0, 4, -1);
        }

        @Test
        @DisplayName("Valores grandes e negativos (ex.: 100, -100) -> -1")
        void extremeOutOfRangeValues() {
            assertJogarReturns(100, -100, -1);
        }
    }
}

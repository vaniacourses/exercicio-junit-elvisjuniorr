package test.java.com.example.calculadora;

import main.java.com.example.calculadora.Calculadora;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculadora - Testes unitários completos (JUnit 5)")
class CalculadoraTest {

    private Calculadora calc;

    @BeforeEach
    void setUp() {
        calc = new Calculadora();
    }

    // -------------------------
    // Soma
    // -------------------------
    @Nested
    @DisplayName("Soma")
    class SomaTests {
        @ParameterizedTest(name = "soma({0}, {1}) == {2}")
        @CsvSource({
                "1, 2, 3",
                "-1, -2, -3",
                "5, -3, 2",
                "0, 0, 0",
                "2147483647, 0, 2147483647"
        })
        void soma_valores_normais_e_limite_sem_overflow(int a, int b, int esperado) {
            // Act
            int resultado = calc.soma(a, b);
            // Assert
            assertEquals(esperado, resultado);
        }

        @Test
        @DisplayName("Soma produz overflow de int (wrap-around esperado)")
        void soma_overflow_wrapAround() {
            int a = Integer.MAX_VALUE;
            int b = 1;
            int resultado = calc.soma(a, b);
            // Valor conhecido do wrap-around: Integer.MAX_VALUE + 1 == Integer.MIN_VALUE
            assertEquals(Integer.MIN_VALUE, resultado);
        }
    }

    // -------------------------
    // Subtração
    // -------------------------
    @Nested
    @DisplayName("Subtração")
    class SubtracaoTests {
        @ParameterizedTest(name = "subtracao({0}, {1}) == {2}")
        @CsvSource({
                "5, 3, 2",
                "3, 5, -2",
                "-5, -3, -2",
                "0, 0, 0"
        })
        void subtracao_casos_basicos(int a, int b, int esperado) {
            int resultado = calc.subtracao(a, b);
            assertEquals(esperado, resultado);
        }

        @Test
        @DisplayName("Subtração que resulta em underflow (wrap-around)")
        void subtracao_overflow_wrapAround() {
            int a = Integer.MIN_VALUE;
            int b = 1;
            int resultado = calc.subtracao(a, b);
            // Integer.MIN_VALUE - 1 -> wrap-around -> Integer.MAX_VALUE
            assertEquals(Integer.MAX_VALUE, resultado);
        }
    }

    // -------------------------
    // Multiplicação
    // -------------------------
    @Nested
    @DisplayName("Multiplicação")
    class MultiplicacaoTests {
        @ParameterizedTest(name = "multiplicacao({0}, {1}) == {2}")
        @CsvSource({
                "2, 3, 6",
                "0, 12345, 0",
                "-4, 5, -20",
                "-3, -3, 9"
        })
        void multiplicacao_casos_normais(int a, int b, int esperado) {
            int resultado = calc.multiplicacao(a, b);
            assertEquals(esperado, resultado);
        }

        @Test
        @DisplayName("Multiplicação que causa overflow (wrap-around conhecido)")
        void multiplicacao_overflow_wrapAround() {
            int a = Integer.MAX_VALUE;
            int b = 2;
            int resultado = calc.multiplicacao(a, b);
            // Valor esperado por overflow em int: (int)( (long)a * b )
            int esperado = (int)((long)a * (long)b);
            assertEquals(esperado, resultado);
        }
    }

    // -------------------------
    // Divisão
    // -------------------------
    @Nested
    @DisplayName("Divisão")
    class DivisaoTests {
        @ParameterizedTest(name = "divisao({0}, {1}) == {2}")
        @CsvSource({
                "6, 3, 2",
                "7, 3, 2",
                "-7, 3, -2",
                "7, -3, -2",
                "0, 5, 0"
        })
        void divisao_casos_normais(int a, int b, int esperado) {
            int resultado = calc.divisao(a, b);
            assertEquals(esperado, resultado);
        }

        @Test
        @DisplayName("Divisão por zero deve lançar ArithmeticException")
        void divisao_por_zero_lanca_arithmeticException() {
            assertThrows(ArithmeticException.class, () -> calc.divisao(5, 0));
        }
    }

    // -------------------------
    // Somatória
    // -------------------------
    @Nested
    @DisplayName("Somatória (0 até n)")
    class SomatoriaTests {
        @Test
        void somatoria_zero() {
            assertEquals(0, calc.somatoria(0));
        }

        @Test
        void somatoria_um() {
            assertEquals(1, calc.somatoria(1));
        }

        @Test
        void somatoria_tres() {
            assertEquals(6, calc.somatoria(3));
        }

        @Test
        void somatoria_negativo_retorna_zero() {
            assertEquals(0, calc.somatoria(-5));
        }
    }

    // -------------------------
    // ehPositivo
    // -------------------------
    @Nested
    @DisplayName("ehPositivo")
    class EhPositivoTests {
        @Test
        void ehPositivo_para_positivo() {
            assertTrue(calc.ehPositivo(5));
        }

        @Test
        void ehPositivo_para_zero() {
            assertTrue(calc.ehPositivo(0));
        }

        @Test
        void ehPositivo_para_negativo() {
            assertFalse(calc.ehPositivo(-1));
        }
    }

    // -------------------------
    // compara
    // -------------------------
    @Nested
    @DisplayName("compara")
    class ComparaTests {
        @Test
        void compara_a_igual_b_retorna_zero() {
            assertEquals(0, calc.compara(5, 5));
        }

        @Test
        void compara_a_maior_b_retorna_um() {
            assertEquals(1, calc.compara(10, 5));
        }

        @Test
        void compara_a_menor_b_retorna_menos_um() {
            assertEquals(-1, calc.compara(3, 7));
        }
    }
}

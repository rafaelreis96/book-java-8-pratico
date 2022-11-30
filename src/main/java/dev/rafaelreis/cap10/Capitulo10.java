package dev.rafaelreis.cap10;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class Capitulo10 {
    public static void main(String[] args) {

        /* 10.2 Trabalhando com datas de forma fluente */

        //antiga
        Calendar mesQueVem = Calendar.getInstance();
        mesQueVem.add(Calendar.MONTH, 1);

        //com java.time
        LocalDate mesQueVem2 = LocalDate.now().plusMonths(1);

        //subtrair ano
        LocalDate anoPassado = LocalDate.now().minusYears(1);

        //Representar Data e Hora
        LocalDateTime agora = LocalDateTime.now();
        System.out.println(agora);


        //Representar Hora
        LocalTime agora2 = LocalTime.now();
        System.out.println(agora2);

        //Criado LocalDateTime com LocalDate + atTime
        LocalDateTime hojeAoMeioDia = LocalDate.now().atTime(12, 0);

        //Combinando Data e Hora
        //sempre podemos utilizar os método at para combinar os diferentes modelos
        LocalTime agora3 = LocalTime.now();
        LocalDate hoje3 = LocalDate.now();
        LocalDateTime dataEHora = hoje3.atTime(agora3);
        
        //Adicionando Timezone
        ZonedDateTime dataComHoraETimezone = dataEHora.atZone(ZoneId.of("America/Sao_Paulo"));
    
        //Para converter esses objetos para outras medidas de tempo 
        //podemos utilizar os métodos to
        LocalDateTime semTimeZone = dataComHoraETimezone.toLocalDateTime();

        //nova API contam com o método estático of, que é
        //um factory method para construção de suas novas instâncias 

        LocalDate date = LocalDate.of(2014, 12, 25);
        LocalDateTime dateTime = LocalDateTime.of(2014, 12, 25, 10, 30);

        
        // De forma similar aos setters, os novos modelos imutáveis possuem os métodos
        // do iths para facilitar a inserção de suas informações

        LocalDate diaDoPassado = LocalDate.now().withYear(1988);
        System.out.println(diaDoPassado.getYear());

        //verificar medida de tempo
        LocalDate hoje4 = LocalDate.now();
        LocalDate amanha = LocalDate.now().plusDays(1);

        System.out.println(hoje4.isBefore(amanha));
        System.out.println(hoje4.isAfter(amanha));
        System.out.println(hoje4.isEqual(amanha));

        //Comparaço Datas iguais com Fuso-Jorrios diferentes
        ZonedDateTime tokyo = ZonedDateTime
            .of(2011, 5, 2, 10, 30, 0, 0, ZoneId.of("Asia/Tokyo"));

        ZonedDateTime saoPaulo = ZonedDateTime
            .of(2011, 5, 2, 10, 30, 0, 0, ZoneId.of("America/Sao_Paulo"));

        //false
        System.out.println(tokyo.isEqual(saoPaulo));

        //Para que o resultado do método isEqual de um ZonedDateTime
        //seja true, precisamos acertar a diferença de tempo entre as duas datas

        tokyo = tokyo.plusHours(12);
        
        //true
        System.out.println(tokyo.isEqual(saoPaulo));

        // Usando MonthDay e YearMonth
        System.out.println("Hoje e dia: " + MonthDay.now().getDayOfMonth());

        LocalDate data = LocalDate.now();
        YearMonth ym = YearMonth.from(data);
        System.out.println(ym.getMonth() + " " + ym.getYear());

        /* 0.3 Enums no lugar de constantes */
        System.out.println(LocalDate.of(2014, 12, 26));
        System.out.println(LocalDate.of(2014, Month.DECEMBER, 25));

        //Usando metodos auxiliares
        System.out.println(Month.DECEMBER.firstMonthOfQuarter());
        System.out.println(Month.DECEMBER.plus(2));
        System.out.println(Month.DECEMBER.minus(1));

        //Primeiro dia do trimetre de determinado Mes
        System.out.println("\n" + Month.DECEMBER.firstMonthOfQuarter());
        //Incrementar Meses
        System.out.println(Month.DECEMBER.plus(2));
        //Decrementar Meses
        System.out.println(Month.DECEMBER.minus(1));

        //Para imprimir o nome de mes formatado, podemos utilizar o metodo getDisplayName
        Locale pt = new Locale("pt");
        System.out.println("\n" + Month.DECEMBER.getDisplayName(TextStyle.FULL, pt));
        System.out.println(Month.DECEMBER.getDisplayName(TextStyle.SHORT, pt));

        System.out.println(DayOfWeek.FRIDAY.getDisplayName(TextStyle.FULL, pt));
        System.out.println(DayOfWeek.FRIDAY.getDisplayName(TextStyle.SHORT, pt));
        System.out.println(DayOfWeek.FRIDAY.getDisplayName(TextStyle.FULL_STANDALONE, pt));

        /* 0.4 Formatando com a nova API de datas */

        String resultado = agora.format(DateTimeFormatter.ISO_LOCAL_TIME);
        System.out.println("\n" + resultado);

        //Definido padrao com o metodo ofPattern
        resultado = agora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println(resultado);

        //Transformando String em LocalDate
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        resultado = agora.format(formatador);
        LocalDate agoraEmData = LocalDate.parse(resultado, formatador);

        LocalDate agoraEmData2 = LocalDate
            .parse("05/05/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        /* 10.5 Datas inválidas */

        //Com calendar, simulando exeption
        Calendar instance = Calendar.getInstance();
        instance.set(2014, Calendar.FEBRUARY, 30);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMyy");
        //System.out.println(dateFormat.format(instance.getTime()));
        //resultado e 02/03/2014
        //Calendar ajustou o mês e dia, sem dar nenhum feedback desse erro que provavelmente passaria
        //despercebido.

        //A nova API de datas vai lançar uma DateTimeException em casos como esse

        //LocalDate.of(2014, Month.FEBRUARY, 30);
        //O resultado será: java.time.DateTimeException: Invalid date 'FEBRUARY 30'.

        //LocalDateTime horaInvalida = LocalDate.now().atTime(25, 0);
        //resultado será: java.time.DateTimeException: Invalid value for HourOfDay (valid values 0 - 23): 25.

        /* 10.6 Duração e Período */

        //Com Calendar
        Calendar atualData = Calendar.getInstance();
        Calendar outraData = Calendar.getInstance();
        long diferenca = atualData.getTimeInMillis() - outraData.getTimeInMillis();
        long milisegundosDeUmDia = 1000 * 60 * 60 * 24;
        long dia = diferenca / milisegundosDeUmDia;

        //Com a nova API
        //Usando ChronoUnit
        LocalDate atual = LocalDate.now();
        LocalDate outroData = LocalDate.of(1989, Month.JANUARY, 25);
        long dias = ChronoUnit.DAYS.between(outroData, atual);
        long meses = ChronoUnit.MONTHS.between(outroData, atual);
        long anos = ChronoUnit.YEARS.between(outroData, atual);
        System.out.printf("%s dias, %s meses e %s anos\n", dias, meses, anos);
    
        //Usando Period
        Period periodo = Period.between(outroData, atual);
        System.out.printf("%s dias, %s meses e %s anos\n", 
            periodo.getDays(), periodo.getMonths(), periodo.getYears());

        //resultados negativos
        periodo = Period.between(atual, outroData);
        System.out.printf("%s dias, %s meses e %s anos\n", 
            periodo.getDays(), periodo.getMonths(), periodo.getYears());


        periodo = Period.between(outroData, atual);
        if(periodo.isNegative()) {
            periodo = periodo.negated();//inverte o sinal
        }

        System.out.printf("%s dias, %s meses e %s anos\n", 
            periodo.getDays(), periodo.getMonths(), periodo.getYears());

        periodo = Period.of(2, 10, 5);

        // Period -> Medidas de Datas
        // Duration -> Medidas de Tempo

        LocalDateTime agoraTime = LocalDateTime.now();
        LocalDateTime daquiUmaHora = LocalDateTime.now().plusHours(1);
        Duration duration = Duration.between(agoraTime, daquiUmaHora);

        if(duration.isNegative()) {
            duration = duration.negated();
        }

        System.out.printf("%s horas, %s minutos e %s segundos\n", 
        duration.toHours(), duration.toMinutes(), duration.getSeconds());


        /* 0.7 Diferenças para o Joda Time
        
            É importante lembrar que a nova API de datas (JSR-310) é baseada no Joda-Time,
            mas que não é uma cópia. Existem algumas diferenças de design que foram cuida-
            dosamente apontadas pelo Stephen Colebourne em seu blog, no artigo Why JSR-310
            isn’t Joda-Time:
            http://blog.joda.org/2009/11/why-jsr-310-isn-joda-time_4941.html.
        */

         

    }
}

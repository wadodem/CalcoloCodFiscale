/*

Calcolatore di codice fiscale italiano

11/05/2021

Giacomo Tarli

 */


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.text.WordUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {

    //Directory del file Excel contenente la lista dei comuni italiani e i corrispettivi codici di riferimento.
    private static final String EXCEL_FILE_LOCATION = "C:\\Users\\wadod\\IdeaProjects\\CalcoloCodFisc\\src\\main\\resources\\Comuni.xls";

    //Scanner per input dati.
    static Scanner scanner = new Scanner(System.in);

    private static char codiceMese;
    private static int codiceDiControllo;

    //Metodo di verifica inserimento dati corretti.
    public static boolean checkAlfabeto(String nomeDaControllare) {
        for (int x = 0; x < nomeDaControllare.length(); x++) {
            if (nomeDaControllare.charAt(x) == ' ' || nomeDaControllare.charAt(x) == '\'') {
            } else if (!Character.isLetter(nomeDaControllare.charAt(x))) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {


        /*
         Loop che richiede inserimento cognome finché non ne viene inserito uno
         corretto (solo caratteri dell'alfabeto)
         */

        String cognome;
        while (true) {
            try {
                System.out.print("Inserire il cognome: ");

                cognome = scanner.nextLine();

                //Controlla che venga inserita una stringa come cognome e che essa contenga solo caratteri dell'alfabeto.
                if (cognome.isEmpty() || checkAlfabeto(cognome)) {
                    System.out.println("Inserire un cognome valido!");
                    continue;
                }

                //Modifica il cognome rendendolo maiuscolo.
                cognome = cognome.toUpperCase();

                /*Rimozione di eventuali spazi, apostrofi e accenti per cognomi come "De Luca", "D'Annunzio",
                 "Polà".
                 */
                cognome = cognome.replaceAll("\\s", "");
                cognome = cognome.replaceAll("'", "");
                cognome = Normalizer.normalize(cognome, Normalizer.Form.NFD);
                cognome = cognome.replaceAll("\\p{M}", "");



                /*Creazione della parte di codice fiscale relativo al cognome
                  in base a quante consonanti e vocali contiene.
                 */
                String consonantiCognome = cognome.replaceAll("[aeiouAEIOU]", "");
                String vocaliCognome = cognome.replaceAll("[bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ]", "");


                if (consonantiCognome.length() >= 3) {
                    cognome = consonantiCognome.substring(0, 3).toUpperCase();
                } else if (consonantiCognome.length() == 2 && cognome.length() >= 3) {
                    cognome = consonantiCognome + vocaliCognome.charAt(0);
                } else if (consonantiCognome.length() == 1 && cognome.length() >= 3) {
                    cognome = consonantiCognome + vocaliCognome.substring(0, 2);
                } else if (consonantiCognome.length() == 1 && cognome.length() <= 2) {
                    if (cognome.length() == 2) {
                        cognome = consonantiCognome + vocaliCognome.charAt(0) + 'X';
                    } else if (cognome.length() == 1) {
                        cognome = consonantiCognome + "XX";
                    }
                } else if (consonantiCognome.length() == 0) {
                    if (vocaliCognome.length() >= 3) {
                        cognome = vocaliCognome.substring(0, 3);
                    } else if (vocaliCognome.length() == 2) {
                        cognome = vocaliCognome.substring(0, 2) + 'X';
                    } else if (vocaliCognome.length() == 1) {
                        cognome = vocaliCognome.charAt(0) + "XX";
                    }
                }

                break;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }


        //Loop che richiede inserimento nome; come sopra per il cognome.
        String nome;
        while (true) {
            try {
                System.out.print("Inserire il nome: ");

                nome = scanner.nextLine();


                if (nome.isEmpty() || checkAlfabeto(nome)) {
                    System.out.println("Inserire un cognome valido!");
                    continue;
                }

                //Modifica il nome rendendolo maiuscolo e rimuovendo eventuali spazi, apostrofi, accenti.
                nome = nome.toUpperCase();
                nome = nome.replaceAll("\\s", "");
                nome = nome.replaceAll("'", "");
                nome = Normalizer.normalize(nome, Normalizer.Form.NFD);
                nome = nome.replaceAll("\\p{M}", "");

                //Calcolo di parte di codice fiscale relativa al nome.
                String consonantiNome = nome.replaceAll("[aeiouAEIOU]", "");
                String vocaliNome = nome.replaceAll("[bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ]", "");

                if (consonantiNome.length() >= 4) {
                    nome = consonantiNome.charAt(0) + "" + consonantiNome.charAt(2) + consonantiNome.charAt(3);
                } else if (consonantiNome.length() == 3) {
                    nome = consonantiNome;
                } else if (consonantiNome.length() == 2 && nome.length() >= 3) {
                    nome = consonantiNome + vocaliNome.charAt(0);
                } else if (consonantiNome.length() == 2 && nome.length() == 2) {
                    nome = consonantiNome + 'X';
                } else if (consonantiNome.length() == 1 && nome.length() >= 3) {
                    nome = consonantiNome + vocaliNome.substring(0, 2);
                } else if (consonantiNome.length() == 1 && vocaliNome.length() == 1) {
                    nome = consonantiNome + vocaliNome + 'X';
                } else if (consonantiNome.length() == 1 && nome.length() == 1) {
                    nome = consonantiNome + "XX";
                } else if (consonantiNome.length() == 0 && nome.length() >= 3) {
                    nome = vocaliNome.substring(0, 3);
                } else if (consonantiNome.length() == 0 && nome.length() == 2) {
                    nome = vocaliNome.substring(0, 2) + 'X';
                } else if (consonantiNome.length() == 0 && nome.length() == 1) {
                    nome = vocaliNome.charAt(0) + "XX";
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        /*Inserimento sesso. In base ad esso cambierà la parte di codice relativa
          al giorno di nascita.
         */
        String sesso;
        while (true) {
            System.out.print("Inserire il sesso (M o F): ");
            sesso = scanner.nextLine();
            sesso = sesso.replaceAll("\\s", "");

            //controlla che il sesso sia inserito correttamente
            if (sesso.equalsIgnoreCase("m") || sesso.equalsIgnoreCase("f")) {
                break;
            } else {
                System.out.println("Inserire un sesso valido!");
            }
        }

        //Solo il formato gg/mm/aaaa è richiesto e accettato nell'inserimento data di nascita
        int giornoDiNascita;
        int annoDiNascita;
        int meseDiNascita;
        while (true) {
            System.out.print("Inserire la data di nascita nel formato gg/mm/aaaa: ");
            String dataDiNascita = scanner.nextLine();
            DateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
            formatoData.setLenient(false);
            Date data;
            Calendar calendar;

            try {
                data = formatoData.parse(dataDiNascita);
                calendar = Calendar.getInstance();
                calendar.setTime(data);
                if (calendar.get(Calendar.YEAR) < 1901) {
                    System.out.println("Inserire una data valida e nel formato giusto!");
                    scanner.reset();
                    continue;
                }

            } catch (ParseException pe) {
                System.out.println("Inserire un formato data valido!");
                scanner.reset();
                continue;
            }

            annoDiNascita = calendar.get(Calendar.YEAR);
            String annoDueCifre = String.valueOf(annoDiNascita);
            if (annoDueCifre.length() == 4) {
                annoDueCifre = annoDueCifre.substring(2, 4);
                annoDiNascita = Integer.parseInt(annoDueCifre);
            }
            meseDiNascita = calendar.get(Calendar.MONTH);
            meseDiNascita = meseDiNascita + 1;
            giornoDiNascita = calendar.get(Calendar.DAY_OF_MONTH);

            break;

        }

        /*In base al mese di nascita viene assegnato un codice di una lettera
          che andrà a far parte del codice fiscale.
         */
        switch (meseDiNascita) {
            case 1 -> codiceMese = 'A';
            case 2 -> codiceMese = 'B';
            case 3 -> codiceMese = 'C';
            case 4 -> codiceMese = 'D';
            case 5 -> codiceMese = 'E';
            case 6 -> codiceMese = 'H';
            case 7 -> codiceMese = 'L';
            case 8 -> codiceMese = 'M';
            case 9 -> codiceMese = 'P';
            case 10 -> codiceMese = 'R';
            case 11 -> codiceMese = 'S';
            case 12 -> codiceMese = 'T';
        }

        //Inserimento comune di nascita.
        String comune;
        while (true) {

            System.out.print("Inserire il comune italiano di nascita: ");

            comune = scanner.nextLine();

            if (comune.isEmpty() || checkAlfabeto(comune)) {
                System.out.println("Inserisci un comune valido!");
                continue;
            } else {
                comune = WordUtils.capitalize(comune);
            }

            /*
             Viene utilizzata JExcelAPI per leggere il file .xls esterno contente
             l'elenco dei comuni ed i relativi codici di riferimento.
             */
            Workbook workbook = null;
            try {


                workbook = Workbook.getWorkbook(new File(EXCEL_FILE_LOCATION));

                Sheet sheet = workbook.getSheet(0);
                Cell cell1 = sheet.findCell(comune);
                Cell cell2 = sheet.getCell(1, cell1.getRow());
                comune = cell2.getContents();


            } catch (NullPointerException npe) {
                System.out.println("Inserisci un comune valido!");
                continue;
            } catch (IOException | BiffException e) {
                e.printStackTrace();
            } finally {

                if (workbook != null) {
                    workbook.close();
                }
            }
            break;
        }

        //Parte di cod. fiscale relativo al giorno di nascita.
        String giorno;
        if (sesso.equalsIgnoreCase("m")) {
            giorno = String.format("%02d", giornoDiNascita);
        } else {
            giorno = String.valueOf(giornoDiNascita) + 40;
        }

        String codiceFiscale = cognome + nome + annoDiNascita + codiceMese + giorno + comune;

        //Calcolo dell'ultimo carattere del codice fiscale.
        for (int x = 0; x < codiceFiscale.length(); x += 2) {
            switch (codiceFiscale.charAt(x)) {
                case 'A', '0' -> codiceDiControllo += 1;
                case 'B', '1' -> {
                }
                case 'C', '2' -> codiceDiControllo += 5;
                case 'D', '3' -> codiceDiControllo += 7;
                case 'E', '4' -> codiceDiControllo += 9;
                case 'F', '5' -> codiceDiControllo += 13;
                case 'G', '6' -> codiceDiControllo += 15;
                case 'H', '7' -> codiceDiControllo += 17;
                case 'I', '8' -> codiceDiControllo += 19;
                case 'J', '9' -> codiceDiControllo += 21;
                case 'K' -> codiceDiControllo += 2;
                case 'L' -> codiceDiControllo += 4;
                case 'M' -> codiceDiControllo += 18;
                case 'N' -> codiceDiControllo += 20;
                case 'O' -> codiceDiControllo += 11;
                case 'P' -> codiceDiControllo += 3;
                case 'Q' -> codiceDiControllo += 6;
                case 'R' -> codiceDiControllo += 8;
                case 'S' -> codiceDiControllo += 12;
                case 'T' -> codiceDiControllo += 14;
                case 'U' -> codiceDiControllo += 16;
                case 'V' -> codiceDiControllo += 10;
                case 'W' -> codiceDiControllo += 22;
                case 'X' -> codiceDiControllo += 25;
                case 'Y' -> codiceDiControllo += 24;
                case 'Z' -> codiceDiControllo += 23;
            }
        }

        for (int x = 1; x < codiceFiscale.length(); x += 2) {

            switch (codiceFiscale.charAt(x)) {
                case 'A', '0' -> {
                }
                case 'B', '1' -> codiceDiControllo += 1;
                case 'C', '2' -> codiceDiControllo += 2;
                case 'D', '3' -> codiceDiControllo += 3;
                case 'E', '4' -> codiceDiControllo += 4;
                case 'F', '5' -> codiceDiControllo += 5;
                case 'G', '6' -> codiceDiControllo += 6;
                case 'H', '7' -> codiceDiControllo += 7;
                case 'I', '8' -> codiceDiControllo += 8;
                case 'J', '9' -> codiceDiControllo += 9;
                case 'K' -> codiceDiControllo += 10;
                case 'L' -> codiceDiControllo += 11;
                case 'M' -> codiceDiControllo += 12;
                case 'N' -> codiceDiControllo += 13;
                case 'O' -> codiceDiControllo += 14;
                case 'P' -> codiceDiControllo += 15;
                case 'Q' -> codiceDiControllo += 16;
                case 'R' -> codiceDiControllo += 17;
                case 'S' -> codiceDiControllo += 18;
                case 'T' -> codiceDiControllo += 19;
                case 'U' -> codiceDiControllo += 20;
                case 'V' -> codiceDiControllo += 21;
                case 'W' -> codiceDiControllo += 22;
                case 'X' -> codiceDiControllo += 23;
                case 'Y' -> codiceDiControllo += 24;
                case 'Z' -> codiceDiControllo += 25;
            }
        }

        codiceDiControllo = codiceDiControllo % 26;

        switch (codiceDiControllo) {
            case 0 -> codiceFiscale = codiceFiscale.concat("A");
            case 1 -> codiceFiscale = codiceFiscale.concat("B");
            case 2 -> codiceFiscale = codiceFiscale.concat("C");
            case 3 -> codiceFiscale = codiceFiscale.concat("D");
            case 4 -> codiceFiscale = codiceFiscale.concat("E");
            case 5 -> codiceFiscale = codiceFiscale.concat("F");
            case 6 -> codiceFiscale = codiceFiscale.concat("G");
            case 7 -> codiceFiscale = codiceFiscale.concat("H");
            case 8 -> codiceFiscale = codiceFiscale.concat("I");
            case 9 -> codiceFiscale = codiceFiscale.concat("J");
            case 10 -> codiceFiscale = codiceFiscale.concat("K");
            case 11 -> codiceFiscale = codiceFiscale.concat("L");
            case 12 -> codiceFiscale = codiceFiscale.concat("M");
            case 13 -> codiceFiscale = codiceFiscale.concat("N");
            case 14 -> codiceFiscale = codiceFiscale.concat("O");
            case 15 -> codiceFiscale = codiceFiscale.concat("P");
            case 16 -> codiceFiscale = codiceFiscale.concat("Q");
            case 17 -> codiceFiscale = codiceFiscale.concat("R");
            case 18 -> codiceFiscale = codiceFiscale.concat("S");
            case 19 -> codiceFiscale = codiceFiscale.concat("T");
            case 20 -> codiceFiscale = codiceFiscale.concat("U");
            case 21 -> codiceFiscale = codiceFiscale.concat("V");
            case 22 -> codiceFiscale = codiceFiscale.concat("W");
            case 23 -> codiceFiscale = codiceFiscale.concat("X");
            case 24 -> codiceFiscale = codiceFiscale.concat("Y");
            case 25 -> codiceFiscale = codiceFiscale.concat("Z");
        }

        System.out.println("Codice Fiscale: " + codiceFiscale);
    }
}

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

    //directory del file Excel contenente la lista dei comuni italiani e i corrispettivi codici di riferimento
    private static final String EXCEL_FILE_LOCATION = "C:\\Users\\wadod\\IdeaProjects\\CalcoloCodFisc\\src\\main\\resources\\Comuni.xls";

    //variabili che verranno riempite coi dati dell'utente
    private static String cognome;
    private static String nome;
    private static String dataDiNascita;
    private static int annoDiNascita;
    private static int meseDiNascita;
    private static int giornoDiNascita;
    private static char codiceMese;
    private static String giorno;
    private static String sesso;
    private static String comune;
    private static int codiceDiControllo;

    private static String codiceFiscale;

    //scanner per input dati
    static Scanner scanner = new Scanner(System.in);

    //metodo di verifica inserimento dati corretti
    public static boolean checkAlfabeto(String nomeDaControllare) {
        for (int x = 0; x < nomeDaControllare.length(); x++) {
            if (nomeDaControllare.charAt(x) == ' ' || nomeDaControllare.charAt(x) == '\'') {
                continue;
            } else if (!Character.isLetter(nomeDaControllare.charAt(x))) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {


        //loop che richiede inserimento cognome finché non ne viene inserito uno corretto (solo caratteri dell'alfabeto)
        while (true) {
            try {
                System.out.print("Inserire il cognome: ");

                cognome = scanner.nextLine();

                //controlla che il cognome sia inserito correttamente (che sia inserito qualcosa, solo caratteri dell'alfabeto)
                if (cognome.isEmpty() || checkAlfabeto(cognome)) {
                    System.out.println("Inserire un cognome valido!");
                    continue;
                }

                //modifica il cognome rendendolo maiuscolo
                cognome = cognome.toUpperCase();

                //rimozione di eventuali spazi, apostrofi e accenti per cognomi come "De Luca", "D'Annunzio", "Polà"
                cognome = cognome.replaceAll("\\s", "");
                cognome = cognome.replaceAll("'", "");
                cognome = Normalizer.normalize(cognome, Normalizer.Form.NFD);
                cognome = cognome.replaceAll("\\p{M}", "");


                //creazione della parte di codice fiscale relativo al cognome
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
                        cognome = vocaliCognome.substring(0, 1) + "XX";
                    }
                }

                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //loop che richiede inserimento nome finché non ne viene inserito uno corretto (solo caratteri dell'alfabeto)
        while (true) {
            try {
                System.out.print("Inserire il nome: ");

                nome = scanner.nextLine();


                if (nome.isEmpty() || checkAlfabeto(nome)) {
                    System.out.println("Inserire un cognome valido!");
                    continue;
                }

                //modifica il nome rendendolo maiuscolo
                nome = nome.toUpperCase();

                //rimozione di eventuali spazi per chi ha due nomi
                nome = nome.replaceAll("\\s", "");
                nome = nome.replaceAll("'", "");
                nome = Normalizer.normalize(nome, Normalizer.Form.NFD);
                nome = nome.replaceAll("\\p{M}", "");

                //creazione della parte di codice fiscale relativo al cognome
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
            }
        }


        while (true) {
                System.out.print("Inserire il sesso (M o F): ");
                sesso = scanner.nextLine();
                sesso = sesso.replaceAll("\\s", "");

                //controlla che il sesso sia inserito correttamente
                if (sesso.equalsIgnoreCase("m") || sesso.equalsIgnoreCase("f")) {
                    break;
                }
                else{
                    System.out.println("Inserire un sesso valido!");
                    continue;
                }
        }

        while(true){
            System.out.print("Inserire la data di nascita nel formato gg/mm/aaaa: ");
            dataDiNascita = scanner.nextLine();
            DateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
            formatoData.setLenient(false);
            Date data = null;
            Calendar calendar;


try {
    data = formatoData.parse(dataDiNascita);
    calendar = Calendar.getInstance();
    calendar.setTime(data);
    if(calendar.get(Calendar.YEAR) < 1901){
        System.out.println("Inserire una data valida e nel formato giusto!");
        scanner.reset();
        continue;
    }

}catch (ParseException pe){
    System.out.println("Inserire un formato data valido!");
    scanner.reset();
    continue;
}


annoDiNascita = calendar.get(Calendar.YEAR);
String annoDueCifre = String.valueOf(annoDiNascita);
if(annoDueCifre.length() == 4) {
    annoDueCifre = annoDueCifre.substring(2, 4);
    annoDiNascita = Integer.parseInt(annoDueCifre);
}
meseDiNascita = calendar.get(Calendar.MONTH);
meseDiNascita = meseDiNascita + 1;
giornoDiNascita = calendar.get(Calendar.DAY_OF_MONTH);




            break;

        }


        switch (meseDiNascita){
            case 1: codiceMese = 'A';
            break;
            case 2: codiceMese = 'B';
            break;
            case 3: codiceMese = 'C';
            break;
            case 4: codiceMese = 'D';
            break;
            case 5: codiceMese = 'E';
            break;
            case 6: codiceMese = 'H';
            break;
            case 7: codiceMese = 'L';
            break;
            case 8: codiceMese = 'M';
            break;
            case 9: codiceMese = 'P';
            break;
            case 10: codiceMese = 'R';
            break;
            case 11: codiceMese = 'S';
            break;
            case 12: codiceMese = 'T';
        }

        while(true) {

            System.out.print("Inserire il comune italiano di nascita: ");

            comune = scanner.nextLine();

            if (comune.isEmpty() || checkAlfabeto(comune)) {
                System.out.println("Inserisci un comune valido!");
                continue;
            }
            else{
                comune = WordUtils.capitalize(comune);
            }

            //viene utilizzata JExcelAPI per leggere il file .xls esterno contente l'elenco dei comuni ed i relativi codici di riferimento
            Workbook workbook = null;
            try {


                workbook = Workbook.getWorkbook(new File(EXCEL_FILE_LOCATION));

                Sheet sheet = workbook.getSheet(0);
                Cell cell1 = sheet.findCell(comune);
                Cell cell2 = sheet.getCell(1, cell1.getRow());
                comune = cell2.getContents();




            }catch (NullPointerException npe){
                System.out.println("Inserisci un comune valido!");
                continue;
            }

            catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            } finally {

                if (workbook != null) {
                    workbook.close();
                }
            }
break;
        }



                if(sesso.equalsIgnoreCase("m")) {
                    giorno = String.format("%02d", giornoDiNascita);
                }else {
                    giorno = String.valueOf(giornoDiNascita)+40;
                }
                codiceFiscale = cognome + nome + annoDiNascita + codiceMese + giorno + comune;

                for(int x = 0; x < codiceFiscale.length(); x += 2){
                    switch (codiceFiscale.charAt(x)){
                        case 'A', '0': codiceDiControllo += 1;
                        break;

                        case 'B', '1': codiceDiControllo += 0;
                        break;

                        case 'C', '2': codiceDiControllo += 5;
                        break;

                        case 'D', '3': codiceDiControllo += 7;
                        break;

                        case 'E', '4': codiceDiControllo += 9;
                        break;

                        case 'F', '5': codiceDiControllo += 13;
                        break;

                        case 'G', '6': codiceDiControllo += 15;
                        break;

                        case 'H', '7': codiceDiControllo += 17;
                        break;

                        case 'I', '8': codiceDiControllo += 19;
                        break;

                        case 'J', '9': codiceDiControllo += 21;
                        break;

                        case 'K': codiceDiControllo += 2;
                        break;

                        case 'L': codiceDiControllo += 4;
                        break;

                        case 'M': codiceDiControllo += 18;
                        break;

                        case 'N': codiceDiControllo += 20;
                        break;

                        case 'O': codiceDiControllo += 11;
                        break;

                        case 'P': codiceDiControllo += 3;
                        break;

                        case 'Q': codiceDiControllo += 6;
                        break;

                        case 'R': codiceDiControllo += 8;
                        break;

                        case 'S': codiceDiControllo += 12;
                        break;

                        case 'T': codiceDiControllo += 14;
                        break;

                        case 'U': codiceDiControllo += 16;
                        break;

                        case 'V': codiceDiControllo += 10;
                        break;

                        case 'W': codiceDiControllo += 22;
                        break;

                        case 'X': codiceDiControllo += 25;
                        break;

                        case 'Y': codiceDiControllo += 24;
                        break;

                        case 'Z': codiceDiControllo += 23;
                        break;
                    }
                }

                for(int x = 1; x < codiceFiscale.length(); x += 2) {

                    switch (codiceFiscale.charAt(x)) {
                        case 'A', '0':
                            codiceDiControllo += 0;
                            break;

                        case 'B', '1':
                            codiceDiControllo += 1;
                            break;

                        case 'C', '2':
                            codiceDiControllo += 2;
                            break;

                        case 'D', '3':
                            codiceDiControllo += 3;
                            break;

                        case 'E', '4':
                            codiceDiControllo += 4;
                            break;

                        case 'F', '5':
                            codiceDiControllo += 5;
                            break;

                        case 'G', '6':
                            codiceDiControllo += 6;
                            break;

                        case 'H', '7':
                            codiceDiControllo += 7;
                            break;

                        case 'I', '8':
                            codiceDiControllo += 8;
                            break;

                        case 'J', '9':
                            codiceDiControllo += 9;
                            break;

                        case 'K':
                            codiceDiControllo += 10;
                            break;

                        case 'L':
                            codiceDiControllo += 11;
                            break;

                        case 'M':
                            codiceDiControllo += 12;
                            break;

                        case 'N':
                            codiceDiControllo += 13;
                            break;

                        case 'O':
                            codiceDiControllo += 14;
                            break;

                        case 'P':
                            codiceDiControllo += 15;
                            break;

                        case 'Q':
                            codiceDiControllo += 16;
                            break;

                        case 'R':
                            codiceDiControllo += 17;
                            break;

                        case 'S':
                            codiceDiControllo += 18;
                            break;

                        case 'T':
                            codiceDiControllo += 19;
                            break;

                        case 'U':
                            codiceDiControllo += 20;
                            break;

                        case 'V':
                            codiceDiControllo += 21;
                            break;

                        case 'W':
                            codiceDiControllo += 22;
                            break;

                        case 'X':
                            codiceDiControllo += 23;
                            break;

                        case 'Y':
                            codiceDiControllo += 24;
                            break;

                        case 'Z':
                            codiceDiControllo += 25;
                            break;

                    }
                }

                codiceDiControllo = codiceDiControllo%26;

                switch (codiceDiControllo){
                    case 0: codiceFiscale = codiceFiscale.concat("A");
                    break;

                    case 1: codiceFiscale = codiceFiscale.concat("B");
                    break;

                    case 2: codiceFiscale = codiceFiscale.concat("C");
                    break;

                    case 3: codiceFiscale = codiceFiscale.concat("D");
                    break;

                    case 4: codiceFiscale = codiceFiscale.concat("E");
                    break;

                    case 5: codiceFiscale = codiceFiscale.concat("F");
                    break;

                    case 6: codiceFiscale = codiceFiscale.concat("G");
                    break;

                    case 7: codiceFiscale = codiceFiscale.concat("H");
                    break;

                    case 8: codiceFiscale = codiceFiscale.concat("I");
                    break;

                    case 9: codiceFiscale = codiceFiscale.concat("J");
                    break;

                    case 10: codiceFiscale = codiceFiscale.concat("K");
                    break;

                    case 11: codiceFiscale = codiceFiscale.concat("L");
                    break;

                    case 12: codiceFiscale = codiceFiscale.concat("M");
                    break;

                    case 13: codiceFiscale = codiceFiscale.concat("N");
                    break;

                    case 14: codiceFiscale = codiceFiscale.concat("O");
                    break;

                    case 15: codiceFiscale = codiceFiscale.concat("P");
                    break;

                    case 16: codiceFiscale = codiceFiscale.concat("Q");
                    break;

                    case 17: codiceFiscale = codiceFiscale.concat("R");
                    break;

                    case 18: codiceFiscale = codiceFiscale.concat("S");
                    break;

                    case 19: codiceFiscale = codiceFiscale.concat("T");
                    break;

                    case 20: codiceFiscale = codiceFiscale.concat("U");
                    break;

                    case 21: codiceFiscale = codiceFiscale.concat("V");
                    break;

                    case 22: codiceFiscale =  codiceFiscale.concat("W");
                    break;

                    case 23: codiceFiscale = codiceFiscale.concat("X");
                    break;

                    case 24: codiceFiscale = codiceFiscale.concat("Y");
                    break;

                    case 25: codiceFiscale = codiceFiscale.concat("Z");
                    break;
                }

                System.out.println("Codice Fiscale: " + codiceFiscale);
            }
        }
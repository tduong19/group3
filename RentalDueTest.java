import java.io.*;
import java.util.*;

public class RentalDueTest {
    private Scanner x;
    List<SingleFamilyRental> singleList = new ArrayList<SingleFamilyRental>();
    List<ApartmentRental> apartmentList = new ArrayList<ApartmentRental>();
    /**
     * Reads in the file with a scanner.
     */
    public void openFile(){
        try{
            x = new Scanner (new File("rentalDB.txt"));
        }
        catch(FileNotFoundException e){
            System.err.println("Cannot open File rentalDB.txt for reading."
                    + ":No such file or directory.");
        }
        catch(SecurityException e){
            System.err.println("You do not have permission to open this"+
                    "this file " + e);
            
        }
    }
    /**
     * Reads in the text file while checking for exceptions.
     * Depending on whats read, it adds an object to one of two
     * list. singleList and apartmentList.
     */
    public void readFile(){  
        char a = 0;
        String b = null;
        int c = 0;
        while(x.hasNextLine()){
            try{
                 a = x.next().charAt(0);
            }catch(NoSuchElementException e ){
                System.err.println("The value is not a char please try again " +
            e);
            }try{
                 b = x.next();
            }catch(NoSuchElementException e){
                System.err.println("The value is not a string " + e);
            }
            try{
                 c = x.nextInt();
            }catch(NoSuchElementException e){
                
            }      
                if (a == 'S'){
                    singleList.add(new SingleFamilyRental(b,c));
                }
                else if (a == 'A'){
                    apartmentList.add(new ApartmentRental(b,c));         
                }         
        }
    }
    /**
     * Creates a class that implements Comparator.
     * Sorts the Rental Properties by bedroom Count in desending order
      * @author Carlos
     *
     */
    class SortbyBedRoomCount implements Comparator<RentalProperty> 
    { 
        @Override
        public int compare(RentalProperty a, RentalProperty b) 
        { 
            return a.getBedroomCount() > b.getBedroomCount() 
                    ? -1 :(a.getBedroomCount() < b.getBedroomCount() ? 1 : 0);
        } 
    } 
    /**
     * Creates a class that implements Comparator
     * Sorts the Rental Properties by the ID's
     * @author Carlos
     *
     */
    class SortbyID implements Comparator<RentalProperty>
    {
        public int compare(RentalProperty a, RentalProperty b){
            return a.getId().compareTo(b.getId());
        }
    }
    
    /**+
     * 
     * Prints the list of Single-Family Rental and Apartment Rental. Both with 
     * the id's, bedroom count, and rental due for each property after 
     * the increase in price.
     */
    public void print(){
        Collections.sort(singleList, new SortbyID()); 
        Collections.sort(apartmentList, new SortbyID()); 
        Collections.sort(singleList, new SortbyBedRoomCount());
        Collections.sort(apartmentList, new SortbyBedRoomCount());
        System.out.println("Single-Family Rental Summary:");
        System.out.println("House ID Number     # of Bedrooms     Rental Due");
        System.out.println("===============     =============     ==========");
        for(int i =0; i < singleList.size(); i++){
            System.out.println("   " + singleList.get(i).getId() +"     "+
        "        " + singleList.get(i).getBedroomCount() + "             "
        + "  $" + singleList.get(i).getMoneyDue());
        }
        System.out.println("     ...\n");
        System.out.println("Apartment Rental Summary:");
        System.out.println("House ID Number     # of Bedrooms     Rental Due");
        System.out.println("===============     =============     ==========");
        for(int i =0; i < apartmentList.size(); i++){
            System.out.println("   " + apartmentList.get(i).getId() +"     "+
        "        " + apartmentList.get(i).getBedroomCount() + "             "
        + "  $" + apartmentList.get(i).getMoneyDue());
        }
        System.out.println("     ...\n");
        
    }

    public void closeFile(){
        x.close();
    }

    public static void main(String[] args) {
        RentalDueTest r = new RentalDueTest();
        r.openFile();
        r.readFile();
        
        r.print();
        r.closeFile();

}
}

public class SingleFamilyRental extends RentalProperty{
    private int cost[] = new int []{900, 1100, 1300};
    private double increase = 1.04;
   /**
    * constructor for singlefamilyrentals.
    * @param id
    * @param bedroomCount
    */
    public SingleFamilyRental(String id, int bedroomCount) {
        super(id, bedroomCount);
        // TODO Auto-generated constructor stub
    }
    /**
     * Calculates the money that is due depending on the 
     * side of the number of bedrooms.
     * @return cost
     */
    @Override
    public double moneyDue() {
        return cost[getBedroomCount() -1] * increase;
    }

}


public class ApartmentRental extends RentalProperty{
    private int cost[] = new int[]{600, 800};
    private double increase = 1.08;
    /**
     * Constructor for ApartmentRental
     * @param id
     * @param bedroomCount
     */
    public ApartmentRental(String id, int bedroomCount) {
        super(id, bedroomCount);
        // TODO Auto-generated constructor stub
    }

    /**
     * Calculates the money due depending on the bedroom Count.
     * @return cost
     */
    @Override
    public double moneyDue() {     
        return cost[getBedroomCount() -1] * increase;
 
    }

}

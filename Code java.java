// Abstract Class: Vehicle
public abstract class Vehicle {
    // Encapsulated private fields
    private String vehicleId;
    private String model;
    private double baseRentalRate;
    private boolean isAvailable;

    // Constructor
    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        if (vehicleId == null || model == null || baseRentalRate <= 0) {
            throw new IllegalArgumentException("Invalid vehicle details provided.");
        }
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRentalRate = baseRentalRate;
        this.isAvailable = true;
    }

    // Getters and setters
    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getBaseRentalRate() {
        return baseRentalRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // Abstract methods
    public abstract double calculateRentalCost(int days);
    public abstract boolean isAvailableForRental();
}

// Concrete Vehicle Classes
class Car extends Vehicle {
    private boolean hasAirConditioning;

    public Car(String vehicleId, String model, double baseRentalRate, boolean hasAirConditioning) {
        super(vehicleId, model, baseRentalRate);
        this.hasAirConditioning = hasAirConditioning;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        if (hasAirConditioning) {
            cost += 10 * days; // Additional cost for AC
        }
        return cost;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }
}

class Motorcycle extends Vehicle {
    private boolean requiresHelmet;

    public Motorcycle(String vehicleId, String model, double baseRentalRate, boolean requiresHelmet) {
        super(vehicleId, model, baseRentalRate);
        this.requiresHelmet = requiresHelmet;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }
}

class Truck extends Vehicle {
    private double loadCapacity;

    public Truck(String vehicleId, String model, double baseRentalRate, double loadCapacity) {
        super(vehicleId, model, baseRentalRate);
        if (loadCapacity <= 0) {
            throw new IllegalArgumentException("Load capacity must be greater than zero.");
        }
        this.loadCapacity = loadCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days + (loadCapacity * 5 * days); // Cost increases with load capacity
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }
}

// Rentable Interface
interface Rentable {
    void rent(Customer customer, int days);
    void returnVehicle();
}

// Supporting Classes
class Customer {
    private String name;
    private String customerId;

    public Customer(String name, String customerId) {
        if (name == null || customerId == null) {
            throw new IllegalArgumentException("Invalid customer details.");
        }
        this.name = name;
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }
}

class RentalAgency {
    private List<Vehicle> fleet = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public Vehicle findAvailableVehicle(String model) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getModel().equals(model) && vehicle.isAvailableForRental()) {
                return vehicle;
            }
        }
        throw new IllegalStateException("No available vehicle of the specified model.");
    }
}

class RentalTransaction {
    private Customer customer;
    private Vehicle vehicle;
    private int rentalDays;
    private double totalCost;

    public RentalTransaction(Customer customer, Vehicle vehicle, int rentalDays) {
        if (rentalDays <= 0) {
            throw new IllegalArgumentException("Rental days must be positive.");
        }
        this.customer = customer;
        this.vehicle = vehicle;
        this.rentalDays = rentalDays;
        this.totalCost = vehicle.calculateRentalCost(rentalDays);
        vehicle.setAvailable(false);
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void completeRental() {
        vehicle.setAvailable(true);
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {
        // Create a RentalAgency instance
        RentalAgency agency = new RentalAgency();

        // Add vehicles to the agency
        agency.addVehicle(new Car("C001", "Sedan", 50, true));
        agency.addVehicle(new Motorcycle("M001", "Sport Bike", 30, true));
        agency.addVehicle(new Truck("T001", "Cargo Truck", 80, 5));

        // Create a customer
        Customer customer = new Customer("John Doe", "CUST001");

        // Find and rent a vehicle
        try {
            Vehicle vehicle = agency.findAvailableVehicle("Sedan");
            RentalTransaction transaction = new RentalTransaction(customer, vehicle, 3);
            System.out.println("Vehicle rented successfully! Total cost: $" + transaction.getTotalCost());

            // Complete the rental
            transaction.completeRental();
            System.out.println("Vehicle returned successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

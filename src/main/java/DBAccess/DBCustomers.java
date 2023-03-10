package DBAccess;

import Database.JDBC;
import Helper.TimeHelper;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rbrod.scheduleapp.ScheduleApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * DBCustomers is used for CRUD operations within the 'customers' database table.
 *
 * @author Robert Brod
 */
public class DBCustomers {

    /**
     * Populates and return ObservableList Object with all rows in 'customers' database table. Customer object is created for each row
     * for manipulation and retrieval of data.
     *
     * @return ObservableList containing all row data in database table
     */
    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM customers";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postal = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");

                ZonedDateTime zonedCreateDate;
                try{
                    LocalDate localCreateDate = rs.getDate("Create_Date").toLocalDate();
                    LocalTime localCreateTime = rs.getTime("Create_Date").toLocalTime();
                    zonedCreateDate = ZonedDateTime.of(localCreateDate, localCreateTime, ZoneId.of("UTC"));
                }catch(Exception e){
                    zonedCreateDate = null;
                }


                String createdBy = rs.getString("Created_By");

                ZonedDateTime zonedLastUpdate;
                try{
                    LocalDate localLastUpdateDate = rs.getDate("Last_Update").toLocalDate();
                    LocalTime localLastUpdateTime = rs.getTime("Last_Update").toLocalTime();
                    zonedLastUpdate = ZonedDateTime.of(localLastUpdateDate, localLastUpdateTime, ZoneId.of("UTC"));
                }catch(Exception e){
                    zonedLastUpdate = null;
                }


                String lastUpdatedBy = rs.getString("Last_Updated_By");

                Customer customer = new Customer(id, name, address,postal, phone, zonedCreateDate, createdBy, zonedLastUpdate, lastUpdatedBy, divisionId);
                allCustomers.add(customer);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return allCustomers;
    }

    /**
     * Populates and returns Customer Object with row in 'customers' table with matching Customer Name.
     *
     * @param customerName Customer Name
     * @return             Object containing row data in 'customers' table containing Customer Name
     */
    public static Customer getCustomer(String customerName){
        Customer customer = null;

        try{
            String sql = "SELECT * FROM customers WHERE Customer_Name=?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, customerName);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postal = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");

                LocalDate localCreateDate = rs.getDate("Create_Date").toLocalDate();
                LocalTime localCreateTime = rs.getTime("Create_Date").toLocalTime();
                ZonedDateTime zonedCreateDate = ZonedDateTime.of(localCreateDate, localCreateTime, ZoneId.of("UTC"));

                String createdBy = rs.getString("Created_By");

                LocalDate localLastUpdateDate = rs.getDate("Last_Update").toLocalDate();
                LocalTime localLastUpdateTime = rs.getTime("Last_Update").toLocalTime();
                ZonedDateTime zonedLastUpdate = ZonedDateTime.of(localLastUpdateDate, localLastUpdateTime, ZoneId.of("UTC"));

                String lastUpdatedBy = rs.getString("Last_Updated_By");

                customer = new Customer(id, name, address, postal, phone, zonedCreateDate, createdBy, zonedLastUpdate, lastUpdatedBy, divisionId);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }

        return customer;
    }

    /**
     * Populates and returns Customer Object with row in 'customers' table with matching Customer ID.
     *
     * @param customerID Customer ID
     * @return           Object containing row data in 'customers' table containing Customer ID
     */
    public static Customer getCustomer(int customerID){
        Customer customer = null;

        try{
            String sql = "SELECT * FROM customers WHERE Customer_ID=?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, customerID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postal = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");

                LocalDate localCreateDate = rs.getDate("Create_Date").toLocalDate();
                LocalTime localCreateTime = rs.getTime("Create_Date").toLocalTime();
                ZonedDateTime zonedCreateDate = ZonedDateTime.of(localCreateDate, localCreateTime, ZoneId.of("UTC"));

                String createdBy = rs.getString("Created_By");

                LocalDate localLastUpdateDate = rs.getDate("Last_Update").toLocalDate();
                LocalTime localLastUpdateTime = rs.getTime("Last_Update").toLocalTime();
                ZonedDateTime zonedLastUpdate = ZonedDateTime.of(localLastUpdateDate, localLastUpdateTime, ZoneId.of("UTC"));

                String lastUpdatedBy = rs.getString("Last_Updated_By");

                customer = new Customer(id, name, address, postal, phone, zonedCreateDate, createdBy, zonedLastUpdate, lastUpdatedBy, divisionId);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }

        return customer;
    }

    /**
     * Inserts customer into database table. Column data is populated from Customer object.
     *
     * @param customer Customer to be added to 'customers' table
     */
    public static void addCustomer(Customer customer){
        try{
            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Division_ID) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, customer.getName().getValue());
            ps.setString(2, customer.getAddress().getValue());
            ps.setString(3, customer.getPostalCode().getValue());
            ps.setString(4, customer.getPhone().getValue());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZonedDateTime createDate = TimeHelper.getLocalToUTCConversion();
            ps.setString(5, createDate.format(formatter));

            ps.setString(6, ScheduleApplication.user.getName().getValue());
            ps.setInt(7, customer.getDivisionId().getValue());

            ps.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Removes customer from database table.
     *
     * @param customer Customer to be removed from 'customers' table
     */
    public static void removeCustomer(Customer customer){
        try{
            String sql = "DELETE FROM customers WHERE Customer_ID=?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, customer.getId().getValue());

            ps.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates Customer_Name column in database row containing Customer ID.
     *
     * @param id Customer ID
     * @param name Customer Name
     */
    public static void updateCustomerName(int id, String name){
        try{
            String sql = "UPDATE customers " +
                         "SET Customer_Name = ?, Last_Update = ?, Last_Updated_By = ? " +
                         "WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, name);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZonedDateTime lastUpdate = TimeHelper.getLocalToUTCConversion();
            ps.setString(2, lastUpdate.format(formatter));

            ps.setString(3, ScheduleApplication.user.getName().getValue());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates Address column in database row containing Customer ID.
     *
     * @param id Customer ID
     * @param address Customer Address
     */
    public static void updateCustomerAddress(int id, String address){
        try{
            String sql = "UPDATE customers " +
                    "SET Address = ?, Last_Update = ?, Last_Updated_By = ? " +
                    "WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, address);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZonedDateTime lastUpdate = TimeHelper.getLocalToUTCConversion();
            ps.setString(2, lastUpdate.format(formatter));

            ps.setString(3, ScheduleApplication.user.getName().getValue());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates Postal_Code column in database row containing Customer ID.
     *
     * @param id Customer ID
     * @param postal Customer Postal Code
     */
    public static void updateCustomerPostal(int id, String postal){
        try{
            String sql = "UPDATE customers " +
                    "SET Postal_Code = ?, Last_Update = ?, Last_Updated_By = ? " +
                    "WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, postal);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZonedDateTime lastUpdate = TimeHelper.getLocalToUTCConversion();
            ps.setString(2, lastUpdate.format(formatter));

            ps.setString(3, ScheduleApplication.user.getName().getValue());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates Phone column in database row containing Customer ID.
     *
     * @param id Customer ID
     * @param phone Customer Phone Number
     */
    public static void updateCustomerPhone(int id, String phone){
        try{
            String sql = "UPDATE customers " +
                    "SET Phone = ?, Last_Update = ?, Last_Updated_By = ? " +
                    "WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, phone);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZonedDateTime lastUpdate = TimeHelper.getLocalToUTCConversion();
            ps.setString(2, lastUpdate.format(formatter));

            ps.setString(3, ScheduleApplication.user.getName().getValue());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates Division_ID column in database row containing Customer ID.
     *
     * @param id Customer ID
     * @param divisionId Customer Division ID
     */
    public static void updateCustomerDivisionId(int id, int divisionId){
        try{
            String sql = "UPDATE customers " +
                    "SET Division_ID = ?, Last_Update = ?, Last_Updated_By = ? " +
                    "WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, divisionId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZonedDateTime lastUpdate = TimeHelper.getLocalToUTCConversion();
            ps.setString(2, lastUpdate.format(formatter));

            ps.setString(3, ScheduleApplication.user.getName().getValue());
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}

package lab3;

import journal.Journal;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JDBCStorageDAO extends Firm {
    Connection dbConnection= null;
    JDBCStorageDAO(String name, Journal journal){
        super(name,journal);
    }

    public Connection connectDB() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (ClassNotFoundException e){}
        try {
            dbConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","HA","131827");
        }catch (SQLException e){}
        return dbConnection;
    }

    public  void closeConnection(){
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (Exception e) {
        }
    }

    public Firm readData(){
        readProjects();
        readCustomers();
        readEmoloyees();
        readManagers();
        return this;
    }

    private void readManagers(){
        String selectManagers = "SELECT E.NAME\n" +
                "FROM EMPLOYEES E\n" +
                "JOIN PROJECTS P\n" +
                "ON P.MANAGER_ID=E.EMPLOYEE_ID";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectManagers);
            while (resultSet.next()){
                Employee employee = addEmployee(resultSet.getString("NAME"));
                employee.makeManager();
            }
        }catch (SQLException e){}

    }

    private void readEmoloyees(){
        String selectEmployees="SELECT E.NAME\n" +
                "FROM EMPLOYEES E";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectEmployees);
            while (resultSet.next()){
                addEmployee(resultSet.getString("NAME"));
            }
        }catch (SQLException e){}
    }

    private void readCustomers(){
        String selectCustomers = "SELECT C.NAME\n" +
                "FROM CUSTOMERS C";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectCustomers);
            while (resultSet.next()){
                addCustomer(resultSet.getString("NAME"));
            }
        }catch (SQLException e){}
    }

    private void readProjects(){
        String selectProjects="SELECT P.NAME as PNAME, C.NAME as CNAME, E.NAME as MNAME\n" +
                "FROM PROJECTS P\n" +
                "JOIN EMPLOYEES E\n" +
                "ON E.EMPLOYEE_ID=P.MANAGER_ID\n" +
                "JOIN CUSTOMERS C\n" +
                "ON C.CUSTOMER_ID=P.CUSTOMER_ID";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectProjects);
            while (resultSet.next()){
                Customer customer = addCustomer(resultSet.getString("CNAME"));
                Employee manager = addEmployee(resultSet.getString("MNAME"));
                addProject(resultSet.getString("PNAME"),customer,manager);
            }
        }catch (SQLException e){}
    }

    @Override
    public ArrayList<Employee> getExecutorsOfProject(Project project){//not include manager of project
        ArrayList<Employee> result = new ArrayList<>();

        String selectEmployeesOfProject ="SELECT EMPLOYEES.NAME \n" +
                "  FROM EMPLOYEES \n" +
                "  INNER JOIN EMP_PROJ\n" +
                "  ON EMPLOYEES.EMPLOYEE_ID = EMP_PROJ.EMPLOYEE_ID\n" +
                "  INNER JOIN PROJECTS\n" +
                "  ON EMP_PROJ.PROJECT_ID=PROJECTS.PROJECT_ID\n" +
                "  WHERE PROJECTS.NAME='"+project.getName()+"'";
        String selectManagerOfProject = "  SELECT EMPLOYEES.NAME as MANAGERNAME // селект менеджера\n" +
                "  FROM EMPLOYEES \n" +
                "  JOIN PROJECTS\n" +
                "  ON EMPLOYEES.EMPLOYEE_ID = PROJECTS.MANAGER_ID\n" +
                "  WHERE PROJECTS.NAME='"+project.getName()+"'";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectEmployeesOfProject);
            while (resultSet.next()){
                result.add(new Employee(resultSet.getString("NAME")));
            }
            resultSet = statement.executeQuery(selectManagerOfProject);
            while (resultSet.next()){
                result.add(new Manager(resultSet.getString("MANAGERNAME")));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Project> getProjectsOfEmployee(Employee employee) throws SQLException {//not function select projects of manager
        ArrayList<Project> result = new ArrayList<>();
        String selectProjectsOfEmployee="SELECT P.NAME as PNAME, C.NAME as CNAME, P.MANAGER_ID\n" +
                "  FROM PROJECTS P\n" +
                "  JOIN CUSTOMERS C\n" +
                "  ON P.CUSTOMER_ID=C.CUSTOMER_ID\n" +
                "  INNER JOIN EMP_PROJ EP\n" +
                "  ON P.PROJECT_ID = EP.PROJECT_ID\n" +
                "  INNER JOIN EMPLOYEES E\n" +
                "  ON E.EMPLOYEE_ID = EP.EMPLOYEE_ID\n" +
                "  WHERE E.NAME='"+employee.getName()+"'";
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectProjectsOfEmployee);
            while (resultSet.next()) {
                Customer customer = new Customer(resultSet.getString("CNAME"));
                String managerID = resultSet.getString("MANAGER_ID");
                String managerName = "";
                String selectEmployeeWithManagerID ="SELECT E.NAME as MANAGERNAME\n" +
                        "  FROM EMPLOYEES E\n" +
                        "  JOIN PROJECTS P\n" +
                        "  ON E.EMPLOYEE_ID = P.MANAGER_ID\n" +
                        "  WHERE P.MANAGER_ID='"+managerID+"'";
                Statement statement1 = dbConnection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(selectEmployeeWithManagerID);
                while (resultSet1.next()){
                    managerName=resultSet1.getString("MANAGERNAME");
                }
                Manager manager = new Manager(managerName);
                Project project = new Project(resultSet.getString("PNAME"),customer, manager);
                result.add(project);

            }
        }catch (SQLException e){}
        return result;
    }

    @Override
    public ArrayList<Employee> getEmployeeWithoutProject() throws SQLException {
        ArrayList<Employee> result = new ArrayList<>();
        String selectEmployeeWithoutProject="SELECT EMPLOYEES.NAME\n" +
                "  FROM EMPLOYEES\n" +
                "  WHERE EMPLOYEE_ID NOT IN \n" +
                "            (SELECT EMP_PROJ.EMPLOYEE_ID\n" +
                "             FROM EMP_PROJ)   \n" +
                "             AND\n" +
                "       EMPLOYEE_ID NOT IN\n" +
                "            (SELECT P.MANAGER_ID\n" +
                "             FROM PROJECTS P)  ";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectEmployeeWithoutProject);
            while (resultSet.next()){
                result.add(new Employee(resultSet.getString("NAME")));
            }

        }catch (SQLException e){}
        return result;
    }

    @Override
    public ArrayList<Employee> getEmployeeManagedBy(Manager manager) throws SQLException {
        ArrayList<Employee> result = new ArrayList<>();
        String selectEmployeeManagedBy ="SELECT EM.NAME\n" +
                "  FROM EMPLOYEES EM\n" +
                "  JOIN EMP_PROJ EP\n" +
                "  ON EP.EMPLOYEE_ID = EM.EMPLOYEE_ID\n" +
                "  WHERE EP.PROJECT_ID IN\n" +
                "        (SELECT P.PROJECT_ID  \n" +
                "        FROM PROJECTS P\n" +
                "        JOIN EMPLOYEES E\n" +
                "        ON P.MANAGER_ID=E.EMPLOYEE_ID\n" +
                "        WHERE E.NAME='"+manager.getName()+"'";
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectEmployeeManagedBy);
            while (resultSet.next()) {
                result.add(new Employee(resultSet.getString("NAME")));
            }
        }catch (SQLException e){}
        return result;
    }

    @Override
    public ArrayList<Manager> getManagersOfExecutor(Employee employee){
        ArrayList<Manager> result = new ArrayList<>();
        String selectManagersOfExecutor = "\n" +
                "SELECT E.NAME\n" +
                "FROM EMPLOYEES E\n" +
                "JOIN PROJECTS P\n" +
                "ON P.MANAGER_ID = E.EMPLOYEE_ID\n" +
                "WHERE P.PROJECT_ID IN\n" +
                "(SELECT EMP_PROJ.PROJECT_ID\n" +
                "        FROM EMP_PROJ \n" +
                "        JOIN EMPLOYEES\n" +
                "        ON EMPLOYEES.EMPLOYEE_ID = EMP_PROJ.EMPLOYEE_ID\n" +
                "        WHERE EMPLOYEES.NAME='"+employee.getName()+"'";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectManagersOfExecutor);
            while (resultSet.next()){
                result.add(new Manager(resultSet.getString("NAME")));
            }
        }catch (SQLException e){}
        return result;
    }

    @Override
    public ArrayList<Employee> getCollaborators(Employee employee){
        ArrayList<Employee> result = new ArrayList<>();
        String selectCollaborators = "SELECT E.NAME\n" +
                "FROM EMPLOYEES E \n" +
                "JOIN EMP_PROJ EP\n" +
                "ON EP.EMPLOYEE_ID = E.EMPLOYEE_ID\n" +
                "WHERE EP.PROJECT_ID IN\n" +
                "        (SELECT EMP_PROJ.PROJECT_ID\n" +
                "        FROM EMP_PROJ \n" +
                "        JOIN EMPLOYEES\n" +
                "        ON EMPLOYEES.EMPLOYEE_ID = EMP_PROJ.EMPLOYEE_ID\n" +
                "        WHERE EMPLOYEES.NAME='emp1')\n" +
                "AND \n" +
                "E.NAME<>'"+employee.getName()+"'";
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectCollaborators);
            while (resultSet.next()){
                result.add(new Employee(resultSet.getString("NAME")));
            }
        }catch (SQLException e){}
        return result;
    }

    @Override
    public ArrayList<Project> getProjectsForCustomer(Customer customer){
        ArrayList<Project> result = new ArrayList<>();
        String selectProjectsForCustomer = "SELECT P.NAME as PNAME, E.NAME as MANAGERNAME\n" +
                "FROM PROJECTS P\n" +
                "JOIN EMPLOYEES E\n" +
                "ON E.EMPLOYEE_ID=P.MANAGER_ID\n" +
                "JOIN CUSTOMERS C\n" +
                "ON C.CUSTOMER_ID=P.CUSTOMER_ID\n" +
                "WHERE C.NAME='"+customer.getName()+"'";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectProjectsForCustomer);
            while (resultSet.next()){
                Manager manager = new Manager(resultSet.getString("MANAGERNAME"));
                result.add(new Project(resultSet.getString("PNAME"),customer,manager));
            }
        }catch (SQLException e){}
        return result;
    }

    @Override
    public ArrayList<Employee> getEmployeesForCustomer(Customer customer){
        ArrayList<Employee> result = new ArrayList<>();
        String selectEmployeesForCustomer = "SELECT DISTINCT E.NAME\n" +
                "FROM EMPLOYEES E \n" +
                "JOIN EMP_PROJ EP\n" +
                "ON EP.EMPLOYEE_ID = E.EMPLOYEE_ID\n" +
                "WHERE EP.PROJECT_ID IN\n" +
                "    (SELECT P.PROJECT_ID\n" +
                "    FROM PROJECTS P\n" +
                "    JOIN CUSTOMERS C\n" +
                "    ON C.CUSTOMER_ID=P.CUSTOMER_ID\n" +
                "    WHERE C.NAME='"+customer.getName()+"'";
        String selectManagersForCustomer = "SELECT E.NAME\n" +
                "FROM EMPLOYEES E\n" +
                "JOIN PROJECTS P\n" +
                "ON P.MANAGER_ID = E.EMPLOYEE_ID\n" +
                "WHERE P.PROJECT_ID IN\n" +
                "    (SELECT P.PROJECT_ID\n" +
                "    FROM PROJECTS P\n" +
                "    JOIN CUSTOMERS C\n" +
                "    ON C.CUSTOMER_ID=P.CUSTOMER_ID\n" +
                "    WHERE C.NAME='cust1') ;";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectEmployeesForCustomer);
            while (resultSet.next()){
                result.add(new Employee(resultSet.getString("NAME")));
            }
            resultSet = statement.executeQuery(selectManagersForCustomer);
            while (resultSet.next()){
                result.add(new Manager(resultSet.getString("NAME")));
            }
        }catch (SQLException e){}
        return result;
    }


    public void addEmployeeJDBC(String name) throws SQLException {
        Statement statement = dbConnection.createStatement();
        String insertTableSQL = "INSERT INTO EMPLOYEES (NAME) VALUES ('"+name+"')";
        statement.executeUpdate(insertTableSQL);
    }

    public void addCustomerJDBC(String name) throws SQLException {
        Statement statement = dbConnection.createStatement();
        String insertTableSQL = "INSERT INTO CUSTOMERS (NAME) VALUES ('"+name+"')";
        statement.executeUpdate(insertTableSQL);
    }

    public void addProjectJDBC(String name, Customer customer, Employee manager) throws SQLException {
        String customerID="";
        String managerID="";
        String selectCustomerID="SELECT C.CUSTOMER_ID\n" +
                "FROM CUSTOMERS C\n" +
                "WHERE C.NAME='"+customer.getName()+"'";
        String selectManagerID="SELECT E.EMPLOYEE_ID\n" +
                "FROM EMPLOYEES E\n" +
                "WHERE E.NAME='"+manager.getName()+"'";
        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectCustomerID);
            if (!resultSet.next() ) {
                System.out.println("Customer doesn`t exist");
            }
            customerID=resultSet.getString("CUSTOMER_ID");

            resultSet = statement.executeQuery(selectManagerID);
            if (!resultSet.next() ) {
                System.out.println("Manager doesn`t exist");
            }
            managerID=resultSet.getString("EMPLOYEE_ID");

            String insertTableSQL = "INSERT INTO PROJECTS (NAME,CUSTOMER_ID,MANAGER_ID) VALUES ('"+name+"','"+customerID+"','"+managerID+"')";
            statement.executeUpdate(insertTableSQL);
        }catch (SQLException e){}
    }

    public void addExecutorForProjectJDBC(Project project, Employee executor){
        String executorID="";
        String projectID="";
        String selectProjectID="SELECT P.PROJECT_ID\n" +
                "FROM PROJECTS P\n" +
                "WHERE P.NAME='"+project.getName()+"'";
        String selectExecutorID="SELECT E.EMPLOYEE_ID\n" +
                "FROM EMPLOYEES E\n" +
                "WHERE E.NAME='"+executor.getName()+"'";

        try{
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectProjectID);
            if (!resultSet.next() ) {
                System.out.println("Project doesn`t exist");
            }
            projectID=resultSet.getString("PROJECT_ID");

            resultSet = statement.executeQuery(selectExecutorID);
            if (!resultSet.next() ) {
                System.out.println("Employee doesn`t exist");
            }
            executorID=resultSet.getString("EMPLOYEE_ID");

            String insertTableSQL = "INSERT INTO EMP_PROJ (EMPLOYEE_ID,PROJECT_ID) VALUES ('"+executorID+"','"+projectID+"')";
            statement.executeUpdate(insertTableSQL);
        }catch (SQLException e){}
    }

}

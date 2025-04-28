package Model;

//deze klasse regelt de verbindig tussen mijn java en mySQL database
//hierdoor kan java met de database communiceren
//het is herbruikbaar en waar een database verbindig nodig is kan je
//DatabaseConnector.connect() aanroepen.

import java.sql.Connection; //met de database praten.
import java.sql.DriverManager;//de echte verbinding
import java.sql.SQLException;//foutmeldingen

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/scrum_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    //toegang tot het echte databank kanaal
    public static Connection connect() throws SQLException {
        //een verbinding aanvragen via de driver.Als alles klopt dan krijg je een Connection object terug
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

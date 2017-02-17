package hello;

import org.apache.commons.cli.*;
import org.apache.commons.cli.ParseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class HelloWorld {
   public static void main(String[] args) {
        Properties info = new Properties();
        info.put("user", "AKIAIZ7L6H66SLPRL46Q");
        info.put("password", "46qXJb3OTiAiRxIoQFWXDu8D0tjiv5OaJ0i04tO1");
//            info.put("aws_credentials_provider_class", "com.amazonaws.auth.PropertiesFileCredentialsProvider");
//            info.put("aws_credentials_provider_arguments", "c:\\users\\gtong\\.aws\\credentials\\");
        info.put("s3_staging_dir", "s3://garytest123/");
        Connection connection= null;
        Statement statement = null;
        try {
            getInput(args);
            connection = DriverManager.getConnection("jdbc:awsathena://athena.us-east-1.amazonaws.com:443", info);
            statement = connection.createStatement();
         //   org.apache.log4j.BasicConfigurator.configure();

            Class.forName("com.amazonaws.athena.jdbc.AthenaDriver");

            final boolean hasResults = statement.execute("select region, eventtype, timestamp1, timestamp2 from garytest.gary_table where region <> '' limit 3 ");
            if (hasResults) {
                final ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    final String region = resultSet.getString("region");
                    final String eventtype = resultSet.getString("eventtype");
                    final int timestamp1 = resultSet.getInt("timestamp1");
                    final int timestamp2 = resultSet.getInt("timestamp2");
                    System.out.format("Region %s\neventtype: %s\ntimestamp1: %d\ntimestamp2: %d", region, eventtype, timestamp1, timestamp2);
                }
            } else {
                System.out.println("Has no results");
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (Exception ex) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.printf("Finished connectivity test.");

    }

    public static void getInput(String [] args)
    {
        // create Options object
        Options options = new Options();
        options.addOption("t", false, "display current time");
        options.addOption("k", true, "AWS Secret Access Key");
        options.addOption("w", true, "AWS Access Key");
        options.addOption("s", true, "AWS S3 bucket url location");
        options.addOption("a", true, "AWS Athena url location");

        try {



            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse( options, args);
            String countryCode = cmd.getOptionValue("k");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Refire2", options);
            if (!cmd.hasOption("k")){
                     System.out.println("hello there");
            }
            else {
                System.out.println("alright ok");
                System.out.println(countryCode);
            }

        }
        catch (ParseException exp){

            System.out.println( "Unexpected exception:" + exp.getMessage() );
        }
    }
}
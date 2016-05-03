package conncat.conncat;

/**
 * Created by nunez on 5/3/2016.
 */
public class AddressParser {
    String getAddress(String address){
        String parsedAddress = "";
        String merced = " merced, ca";
        if (address.contains("SE2") || address.contains("S&E II"))
            parsedAddress = ("Science and Engineering Building 2" + merced);
        else if (address.contains("SE1") || address.contains("S&E I"))
            parsedAddress = ("Science and Engineering Building 1" + merced);
        else if (address.contains("SSM"))
            parsedAddress = ("Social Science and Management building" + merced);
        else if (address.contains("SSB"))
            parsedAddress = ("Student Services Building" + merced);
        else if (address.contains("KL ") || address.equalsIgnoreCase("Library"))
            parsedAddress = ("leo and dottie kolligian library" + merced);
        else if (address.contains("COB"))
            parsedAddress = ("Classroom and Office Bldg" + merced);
        else if (address.contains("wallace dutra amphitheatre") || address.contains("Wallace-Dutra Amphitheatre"))
            parsedAddress = ("Kelley Grove" + merced);
        else if (address.contains("SAAC"))
            parsedAddress = ("Student Activities and Athletics Center" + merced);
        else if (address.contains("Gallo Recreation"))
            parsedAddress = ("Joseph E. Gallo Recreation and Wellness Center" + merced);
        else if (address.contains("Outdoor Center"))
            parsedAddress = ("Student Activities and Athletics Center" + merced);
        else if (address.contains("Crescent Arch"))
            parsedAddress = ("Half Dome" + merced);
        else if (address.contains("California Room"))
            parsedAddress = ("Visitor Center" + merced);
        else if (address.contains("Bobcat Lair"))
            parsedAddress = ("leo and dottie kolligian library" + merced);
        else if (address.contains("The Bowl") || address.contains("South Bowl"))
            parsedAddress = ("South Bowl, Scholars Ln" + merced);
        else if (address.contains("Student Housing"))
            parsedAddress = ("Student Housing Valley Terraces" + merced);
        else if (address.contains("N/A"))
            parsedAddress = ("5200 N Lake Rd" + merced);
        else
            parsedAddress = (address);

        return parsedAddress;
    }
}

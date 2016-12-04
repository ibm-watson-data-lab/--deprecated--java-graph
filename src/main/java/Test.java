import org.apache.wink.json4j.JSONObject;

public class Test {

	public static void main(String[] args) {

		try {
			String[] a = {"2", null, "1"};
			for(String pk : a) {
            if((pk == null) || (pk.trim().length() == 0))
                throw new IllegalArgumentException("Parameter propertyKeys cannot contain null values or empty strings.");
        }
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

	}
}
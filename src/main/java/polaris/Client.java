package polaris;

import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

public class Client
{

    private final String endpoint;

    public Client(final String endpoint)
    {
        this.endpoint = endpoint;
    }

    public final void call(final InputStream data)
    {
        final HttpClient client = new DefaultHttpClient();

        final HttpPost post = new HttpPost(endpoint);
        post.addHeader("SOAPAction", "http://www.polaris.co.uk/XRTEService/2009/03/IXRTEService/ProcessTran");

        try
        {
            post.setEntity(new ByteArrayEntity(ByteStreams.toByteArray(data), ContentType.create("text/xml", "UTF-8")));
            final HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == 200)
            {
                System.out.println("Sucess with: " + endpoint);
            }
            else    {
                System.out.println("Failure with: " + endpoint);
            }

            System.out.println(EntityUtils.toString(response.getEntity()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args)
    {
        final Client production = new Client("http://prod-polaris-1242390441.eu-west-1.elb.amazonaws.com/XRTEService/XRTEService.svc");

        production.call(Client.class.getResourceAsStream("/test.xml"));

        final Client uat = new Client("http://ec2-79-125-63-237.eu-west-1.compute.amazonaws.com/XRTEService/XRTEService.svc");

        uat.call(Client.class.getResourceAsStream("/test.xml"));
    }
}

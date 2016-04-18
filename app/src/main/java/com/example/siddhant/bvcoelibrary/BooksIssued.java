package com.example.siddhant.bvcoelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BooksIssued extends AppCompatActivity
{
    ListView searchResults;
    ArrayList<Book> bookResults = new ArrayList<Book>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_issued);
        searchResults = (ListView)findViewById(R.id.listView2);
        Intent i = getIntent();
        String sessionId = i.getStringExtra("sessionId");
        myAsyncTask m = (myAsyncTask) new myAsyncTask().execute(sessionId);
    }

    class myAsyncTask extends AsyncTask<String,Void,String>
    {
        JSONArray bookList;
        String result;
        String url = new String();
        String textSearch;
        ProgressDialog pd;
        JSONParser jsonParser = new JSONParser();
        Boolean status;
        InputStream is = null;
        JSONObject jObj;


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            bookList = new JSONArray();
            pd = new ProgressDialog(BooksIssued.this);
            pd.setCancelable(false);
            pd.setMessage("Searching...");
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            url = "https://shrouded-island-99834.herokuapp.com/api/student/details";
            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                String sId="bvdempo867pl31gt3o18gdi6os";
                httpGet.setHeader("sessionId",sId);
                HttpResponse httpResponse = httpclient.execute(httpGet);
                is = httpResponse.getEntity().getContent();
                if(is != null) {
                    result = convertInputStreamToString(is);
                    jObj = new JSONObject(result);
                }
                Thread.sleep(3000);
                return ("OK");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return ("Exception Caught");
            }
            //String returnResult = getBookList(url);
            //this.textSearch = params[0];
            //return returnResult;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException
        {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line;
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;

        }

        /*public JSONObject getJSONFromUrl(String url)
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            json = postData.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            is = httpResponse.getEntity().getContent();
            if(is != null) {
                result = convertInputStreamToString(is);
                JSONObject jObj = new JSONObject(result);
                status = jObj.getBoolean("status");
            }
        }*/

        public String getBookList(String url)
        {
            Book tempBook = new Book();
            String matchFound = "N";
            try
            {
                JSONObject json = jObj;
                bookList = json.getJSONArray("bookArray");
                status = json.getBoolean("status");
                if(!status)
                    return ("No result");
                for (int i = 0; i < bookList.length(); i++)
                {
                    tempBook = new Book();
                    JSONObject obj = bookList.getJSONObject(i);
                    obj=obj.getJSONObject("books");
                    tempBook.setbookName(obj.getString("name"));
                    tempBook.setbookAuthor(obj.getString("author"));
                    tempBook.setbookDepartment(obj.getString("domain"));
                    tempBook.setbookEdition(obj.getString("id"));
                    tempBook.setbookQty(obj.getString("id"));
                    bookResults.add(tempBook);
                }

                return ("OK");
            } catch (Exception e)
            {
                e.printStackTrace();
                return ("Exception Caught");
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("Exception Caught"))
            {
                Toast.makeText(getApplicationContext(), "Unable to connect to server", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
            else if(s.equalsIgnoreCase("No result"))
            {
                Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
            else
            {
                searchResults.setAdapter(new SearchResultsAdapter(getApplicationContext(), bookResults));
                pd.dismiss();
            }
        }
    }

    class SearchResultsAdapter extends BaseAdapter
    {
        private LayoutInflater layoutInflater;

        private ArrayList<Book> bookDetails = new ArrayList<Book>();
        int count;
        Context context;

        public SearchResultsAdapter(Context context, ArrayList<Book> book_details)
        {
            layoutInflater = LayoutInflater.from(context);

            this.bookDetails = book_details;
            this.count = book_details.size();
            this.context = context;
        }

        public int getCount()
        {
            return count;
        }

        public Object getItem(int arg0)
        {
            return bookDetails.get(arg0);
        }

        public long getItemId(int arg0)
        {
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;
            Book tempBook = bookDetails.get(position);

            if (convertView == null)
            {
                convertView = layoutInflater.inflate(R.layout.books_layout, null);
                holder = new ViewHolder();
                holder.book_name = (TextView) convertView.findViewById(R.id.book_name);
                holder.book_edition_value = (TextView) convertView.findViewById(R.id.book_editionvalue);
                holder.book_author_value = (TextView) convertView.findViewById(R.id.book_authorvalue);
                holder.book_author = (TextView) convertView.findViewById(R.id.book_author);
                holder.book_edition = (TextView)convertView.findViewById(R.id.book_edition);
                convertView.setTag(holder);
            }
            else
            {
                holder=(ViewHolder)convertView.getTag();
            }

            holder.book_name.setText(tempBook.getbookName());
            holder.book_edition_value.setText(tempBook.getbookEdition());
            holder.book_author_value.setText(tempBook.getBookAuthor());
            holder.book_author.setText("Author: ");
            holder.book_edition.setText("Edition:");
            return convertView;
        }

    }

    static class ViewHolder
    {
        TextView book_name;
        TextView book_author;
        TextView book_author_value;
        TextView book_edition;
        TextView book_edition_value;
    }

}

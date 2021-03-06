package com.example.siddhant.bvcoelibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FrontPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SearchView search;
    ListView searchResults;
    String found="N";

    ArrayList<Book> bookResults = new ArrayList<Book>();
    ArrayList<Book> filteredBookResults = new ArrayList<Book>();
    String sessionId;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        i = getIntent();
        sessionId = i.getStringExtra("sessionId");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        search = (SearchView)findViewById(R.id.searchView);
        search.setQueryHint("Start typing to search...");

        searchResults = (ListView)findViewById(R.id.listView);

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>3)
                {
                    searchResults.setVisibility(View.VISIBLE);
                    myAsyncTask m = (myAsyncTask) new myAsyncTask().execute(newText);
                }
                else
                {
                    searchResults.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    public void filterBookArray(String newText)
    {
        String bName;
        filteredBookResults.clear();
        for(int i=0;i<bookResults.size();i++)
        {
            bName = bookResults.get(i).getbookName().toLowerCase();
            if(bName.contains(newText.toLowerCase()))
            {
                filteredBookResults.add(bookResults.get(i));
            }
        }
    }

    class myAsyncTask extends AsyncTask<String,Void,String>
    {
        JSONArray bookList;
        String url = new String();
        String textSearch;
        ProgressDialog pd;
        JSONParser jsonParser = new JSONParser();
        Boolean status;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            bookList = new JSONArray();
            pd = new ProgressDialog(FrontPage.this);
            pd.setCancelable(false);
            pd.setMessage("Searching...");
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            url = "http://shrouded-island-99834.herokuapp.com/api/books/suggestions?key=" + params[0];
            String returnResult = getBookList(url);
            this.textSearch = params[0];
            return returnResult;
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
                JSONObject json = jsonParser.getJSONFromUrl(url);
                bookList = json.getJSONArray("result");
                status = json.getBoolean("status");
                if(!status)
                    return ("No result");
                for (int i = 0; i < bookList.length(); i++)
                {
                    tempBook = new Book();
                    JSONObject obj = bookList.getJSONObject(i);
                    tempBook.setbookName(obj.getString("name"));
                    tempBook.setbookAuthor(obj.getString("author"));
                    tempBook.setbookDepartment(obj.getString("domain"));
                    tempBook.setbookEdition(obj.getString("id"));
                    tempBook.setbookQty(obj.getString("id"));

                    matchFound = "N";

                    for (int j = 0; j < bookResults.size(); j++)
                    {
                        if (bookResults.get(j).getbookEdition().equals(tempBook.getbookEdition()))   //edition used as book id
                        {
                            matchFound = "Y";
                        }
                    }

                    if (matchFound == "N")
                    {
                        bookResults.add(tempBook);
                    }
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
                filterBookArray(textSearch);
                searchResults.setAdapter(new SearchResultsAdapter(getApplicationContext(), filteredBookResults));
                pd.dismiss();
            }
        }

        /*public void issue(View view)
        {

        }*/
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    class SearchResultsAdapter extends BaseAdapter
    {
        private LayoutInflater layoutInflater;

        private ArrayList<Book> bookDetails = new ArrayList<Book>();
        int count;
        Typeface type;
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
                holder.Issue = (Button)convertView.findViewById(R.id.issue);
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
            holder.Issue.setOnClickListener(new BookIssueClickListener("button_issueBook",tempBook,context));
            return convertView;
        }

        public class BookIssueClickListener implements View.OnClickListener
        {
            String button_name;
            Book book_name;
            int tempQty;
            int tempValue;
            Context context;
            InputStream is;
            Boolean status=false;
            String result;
            String url = "https://shrouded-island-99834.herokuapp.com/api/student/issue";
            JSONObject postData = new JSONObject();

            public BookIssueClickListener(String button_name, Book book_name,Context context)
            {
                this.book_name=book_name;
                this.button_name=button_name;
                this.context=context;
            }

            @Override
            public void onClick(View v)
            {
                class m2AsyncTask extends AsyncTask<Void, Void, Boolean>
                {
                    @Override
                    protected Boolean doInBackground(Void... params)
                    {
                        try
                        {
                            postData.put("bookId", book_name.getbookEdition());                  //replace getBookEdition with getBookId
                        } catch (Exception e)
                        {
                        }
                        try
                        {
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost(url);
                            String json = "";
                            json = postData.toString();
                            StringEntity se = new StringEntity(json);
                            httpPost.setEntity(se);
                            httpPost.setHeader("sessionId", sessionId);
                            httpPost.setHeader("Content-type", "application/json");
                            HttpResponse httpResponse = httpclient.execute(httpPost);
                            is = httpResponse.getEntity().getContent();
                            if (is != null)
                            {
                                result = convertInputStreamToString(is);
                                JSONObject jObj = new JSONObject(result);
                                status = jObj.getBoolean("status");
                            }
                            Thread.sleep(3000);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Boolean success)
                    {
                        super.onPostExecute(success);
                        if (success)
                        {
                            Toast.makeText(getApplicationContext(), "Book has been Issued", Toast.LENGTH_LONG).show();
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "You have already issued this book", Toast.LENGTH_LONG).show();
                        }
                    }
                }
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
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.front_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SignInOrRegister.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.books_issued) {
            i = new Intent(this,BooksIssued.class);
            i.putExtra("sessionId",sessionId);
            startActivity(i);
        } else if (id == R.id.fine) {

        } else if (id == R.id.history) {

        } else if (id == R.id.request_book) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static class ViewHolder
    {
        TextView book_name;
        TextView book_author;
        TextView book_author_value;
        TextView book_edition;
        TextView book_edition_value;
        Button Issue;
    }



}

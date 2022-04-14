package Application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import classes.DefItem;
import classes.Response;
import classes.TrItem;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

    public class Application extends TelegramLongPollingBot {
        //    APIkey&lang=en-ru&text=time
        public static final String Dict_API_Key="dict.1.1.20211128T055417Z.beb0a2b65ccbb82b.85bce6250f3838eb435672a992c57259124b0546";
        public static final String Dict_API_Lombok="https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key="+Dict_API_Key+"&lang=";
        public static final String Dict_API="https://dictionary.yandex.net/api/v1/dicservice.json/getLangs?key="+Dict_API_Key;

        public static final String surname="BaxtiyorTatu_bot";
        public static final String token="2135089667:AAEua3QGB5D_pGvapsS1Pm8eFRxNknjchhg";
        public String cur=null;
        int dicId=-1;
        Gson gson=new Gson();

        public static void main(String[] args) {

            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi=new TelegramBotsApi();
            Application application=new Application();
            try {
                telegramBotsApi.registerBot(application);
            } catch (TelegramApiRequestException e) {
                e.printStackTrace();
            }
        }

        @SneakyThrows
        public void onUpdateReceived(Update update) {
            System.out.println(update.getMessage().getChatId()+"=>"+update.getMessage().getChat().getUserName());
            System.out.println(update.getMessage().getText());
            Message message=update.getMessage();
            String command=message.getText();
            SendMessage sendMessage=new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId());

            URL url= null;
            try {
                url = new URL(Dict_API);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection urlConnection= null;
            try {
                urlConnection = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader= null;
            try {
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> list=gson.fromJson(reader,new TypeToken<List<String>>(){}.getType());

            for (int i = 0; i < list.size(); i++) {
                if (command.equals(list.get(i))){
                    cur=list.get(i);
                    dicId=i;
                }
            }


            if (command.equals("/start")){
                sendMessage.setText("Assalomu Aleykum " + update.getMessage().getChat().getUserName()+" quyidagilardan birini tanlang:");
                sendMessage.setReplyMarkup(getMenu());
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (!command.equals("/start") && cur != null){
                sendMessage.setText("So'z kiriting");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                cur=null;
            }else if (!command.equals("/start") && cur == null){
                URL url1= null;
                try {
                    url1 = new URL(Dict_API_Lombok+list.get(dicId)+"&text="+command);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                URLConnection urlConnection1= null;
                try {
                    urlConnection1 = url1.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader reader1= null;
                try {
                    reader1 = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Response list1=gson.fromJson(reader1,Response.class);

                System.out.println(list1.getDef().get(0).getTr().get(0).getText());

                sendMessage.setText(list1.getDef().get(0).getTr().get(0).getText());
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        public ReplyKeyboardMarkup getMenu(){

            ReplyKeyboardMarkup markup=new ReplyKeyboardMarkup();
            markup.setResizeKeyboard(true);
            markup.setOneTimeKeyboard(true);


            try {
                URL url=new URL(Dict_API);
                URLConnection urlConnection=url.openConnection();
                BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                List<String> list=gson.fromJson(reader,new TypeToken<List<String>>(){}.getType());

                List<KeyboardRow> keyboardRowList=new ArrayList<KeyboardRow>();
                KeyboardRow keyboardRow=new KeyboardRow();
                KeyboardButton btn=null;

                for (int i = 0; i < list.size(); i++) {
                    if (i%6==0){
                        keyboardRowList.add(keyboardRow);
                        keyboardRow=new KeyboardRow();
                    }
                    btn=new KeyboardButton();
                    btn.setText(list.get(i));
                    keyboardRow.add(btn);
                }
                markup.setKeyboard(keyboardRowList);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return markup;
        }

        public String getBotUsername() {
            return this.surname;
        }

        public String getBotToken() {
            return this.token;
        }
}

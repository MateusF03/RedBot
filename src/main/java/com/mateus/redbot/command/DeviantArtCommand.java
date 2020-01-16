package com.mateus.redbot.command;

import com.mateus.redbot.core.command.Command;
import com.mateus.redbot.core.command.CommandCategory;
import com.mateus.redbot.core.command.SubCommand;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mateus.redbot.utils.DaArt;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeviantArtCommand {
    @Command(name = "deviantart", description = "Pega uma imagem aleatória do deviantart", commandCategory = CommandCategory.INFO, args = "(termo de pesquisa)", commandPermission = UserPermission.BASE)
    public static void daCmd(GuildMessageReceivedEvent event, String[] args) {
        String search = String.join("%20", args);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            URLConnection urlConnection = new URL("https://backend.deviantart.com/rss.xml?type=deviation&q=boost%3Apopular%2Fdrawings+" + search).openConnection();
            Document doc = db.parse(urlConnection.getInputStream());
            Element element = doc.getDocumentElement();
            NodeList nodeList = element.getChildNodes();
            List<DaArt> arts = new ArrayList<>();
            NodeList childList = nodeList.item(1).getChildNodes();
            for (int i = 0; i < childList.getLength(); i++) {
                if (childList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) childList.item(i);
                    if (el.getNodeName().contains("item")) {
                        String author = el.getElementsByTagName("media:credit").item(0).getTextContent();
                        String url = el.getElementsByTagName("media:content").item(0).getAttributes().getNamedItem("url").getNodeValue();
                        String title = el.getElementsByTagName("media:title").item(0).getTextContent();
                        String rating = el.getElementsByTagName("media:rating").item(0).getTextContent();
                        boolean adult = rating.equals("adult");
                        arts.add(new DaArt(author, url, title, adult));
                    }
                }
            }
            DaArt randomArt = arts.get(new Random().nextInt(arts.size()));
            if (randomArt.isAdult() && !event.getChannel().isNSFW()) {
                event.getChannel().sendMessage("**A arte é NSFW e o canal não é!**").queue();
                return;
            }
            sendArt(event.getChannel(), randomArt);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    private static void sendArt(TextChannel textChannel, DaArt art) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setAuthor("Resultado da pesquisa:");
        embedBuilder.setTitle(art.getTitle());
        embedBuilder.setImage(art.getUrl());
        embedBuilder.setFooter("Arte criada por: " + art.getAuthor());
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
    @SubCommand(name = "info", description = "Pega a informação de um link do deviantart", args = "(link)", permission = UserPermission.BASE)
    public static void info(GuildMessageReceivedEvent event, String[] args) {
        String url = args[0];
        try {
            URLConnection urlConnection = new URL("https://backend.deviantart.com/oembed?url=" + url).openConnection();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(urlConnection.getInputStream()));
            String thumbnail = (String) jsonObject.get("thumbnail_url");
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.RED);
            embedBuilder.setFooter("Comando pedido por: "+ event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setAuthor("Informações da obra:");
            embedBuilder.setTitle((String) jsonObject.get("title"));
            embedBuilder.setThumbnail(thumbnail);
            JSONObject community = (JSONObject) jsonObject.get("community");
            JSONObject statistics = (JSONObject) community.get("statistics");
            JSONObject attributes = (JSONObject) statistics.get("_attributes");
            long views = (long) attributes.get("views");
            long favorites = (long) attributes.get("favorites");
            long comments = (long) attributes.get("comments");
            long downloads = (long) attributes.get("downloads");
            String authorName = (String) jsonObject.get("author_name");
            embedBuilder.setDescription("**Autor:** "+ "`" + authorName + "`\n" +"**Views:** " + "`" + views + "`\n" + "**Favoritos:** " + "`" + favorites + "`\n" + "**Comentários:** " + "`" + comments + "`\n" + "**Downloads:** " + "`" + downloads + "`");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

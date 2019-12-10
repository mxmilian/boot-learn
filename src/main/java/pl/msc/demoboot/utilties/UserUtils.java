package pl.msc.demoboot.utilties;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pl.msc.demoboot.user.db.User;

public class UserUtils {
    public static String getEmailLoggedUser() {//tutaj jest przechowywany jest email zalogowanego
        String userEmail = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //uchwycenie kontekstu

        if (!(authentication instanceof AnonymousAuthenticationToken)) { //sprawdzamy czy pobrane dane nie sa instancja klasy anonymous
            userEmail = authentication.getName();
        }
        return userEmail;
    }
    final User principal = (User) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

    public User getPrincipal() {
        return principal;
    }

    public static List<User> usersDataLoader(File file) {
        List<User> userList = new ArrayList<User>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("user");
            for(int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    User user = new User();
                    user.setEmail(element.getElementsByTagName("email").item(0).getTextContent());
                    user.setPassword(element.getElementsByTagName("password").item(0).getTextContent());
                    user.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    user.setLastName(element.getElementsByTagName("lastname").item(0).getTextContent());
                    user.setActive(Integer.parseInt(element.getElementsByTagName("active").item(0).getTextContent()));
                    user.setNrRoll(Integer.parseInt(element.getElementsByTagName("nrroli").item(0).getTextContent()));
                    userList.add(user);
                }
            }
        } catch (Exception e) {
            System.out.println("WYJATEK SUKOO");
            e.printStackTrace();
        }
        return userList;
    }
}
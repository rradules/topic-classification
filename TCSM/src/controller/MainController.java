/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import functions.ComputeCRC;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Blogpost;
import model.Blogroll;
import model.Category;
import model.Domain;
import model.Location;
import model.Rawdata;
import model.Stopword;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class MainController {

    private static MainController singleton = null;
    private LocationJpaController locationController;
    private DomainJpaController domainController;
    private BlogrollJpaController blogrollController;
    private BlogpostJpaController blogpostController;
    private RawdataJpaController rawdataController;
    private EntityManagerFactory emf;
    private StopwordJpaController stopwordController;
    private CategoryJpaController categoryController;
    private ComputeCRC computeCRC;

// private constructer    
    private MainController() {

        emf = Persistence.createEntityManagerFactory("Crawler1.0PU");
        locationController = new LocationJpaController(emf);
        domainController = new DomainJpaController(emf);
        blogrollController = new BlogrollJpaController(emf);
        blogpostController = new BlogpostJpaController(emf);
        rawdataController = new RawdataJpaController(emf);
        stopwordController = new StopwordJpaController(emf);
        categoryController = new CategoryJpaController(emf);

        computeCRC = new ComputeCRC();
    }
//singleton pattern

    public static MainController getInstance() {
        if (singleton == null) {
            singleton = new MainController();
        }
        return singleton;
    }
//-----------Location related methods-----------------------------

    public Location findLocationBySuffix(String suffix) {
        return locationController.findBySuffix(suffix);
    }

    public void addLocation(String suffix, String location) throws Exception {
        Location loc = findLocationBySuffix(suffix);

        if (loc == null) {
            Location aux = new Location();
            aux.setSuffix(suffix);
            aux.setLocation(location);
            locationController.create(aux);
        } else {
            throw new Exception("This location already exists in the database.");
        }
    }
//-------------------------------------------------------------------

//-------------Domain related methods--------------------------------    
    public Domain findDomainByName(String name) {
        return domainController.findByDomainName(name);
    }

    public Domain addDomain(String name, Location loc, String robots,
            String description, Date activation) throws Exception {
        Domain dom = findDomainByName(name);

        if (dom == null) {
            Domain aux = new Domain();
            aux.setDomainName(name);
            aux.setIdLocation(loc);
            aux.setRobots(robots);
            aux.setDescription(description);
            aux.setActivation(activation);
            domainController.create(aux);

            return findDomainByName(name);

        } else {
            //throw new Exception("This domain already exists in the database.");
            domainController.edit(dom);
            return dom;
        }
    }

    public void addDomainCategory(String name, String categ) {
        Domain domain = findDomainByName(name);
        Category category = findCategoryByName(categ);
        if (domain != null && category != null) {
            domain.setIdCategory(category);
            try {
                domainController.edit(domain);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

    public List<Domain> findDomainByCategory(String nameCateg) {
        Category category = findCategoryByName(nameCateg);
        return domainController.findByDomainCategory(category);
    }

//---------------------------------------------------------------------
//-------------Blogroll related methods-------------------------------- 
    public List<Blogroll> findBlogrollByIdDomain(Domain domain) {
        return blogrollController.findByIdDomain(domain);
    }

    public Blogroll findBlogrollByDomainAndBlog(Domain domain, String blog) {
        return blogrollController.findByDomainAndName(domain, blog);
    }

    public Blogroll addBlogroll(Domain domain, String blog, int type, int destination) throws Exception {

        Blogroll br = findBlogrollByDomainAndBlog(domain, blog);

        if (br == null) {
            Blogroll aux = new Blogroll();
            aux.setBlog(blog);
            aux.setIdDomain(domain);
            aux.setTypeBlogRoll(type);
            aux.setIddestination(destination);
            blogrollController.create(aux);
            return findBlogrollByDomainAndBlog(domain, blog);

        } else {
            blogrollController.edit(br);
            return br;
            //throw new Exception("This blogroll already exists in the database.");
        }

    }
//---------------------------------------------------------------------
//-------------Blogpost related methods--------------------------------

    public Blogpost findBlogpostByAddress(String address) {
        return blogpostController.findByPageAddress(address);
    }

    public Blogpost addBlogpost(String address, Date date,
            String title, String content,
            String description, Domain domain) throws Exception {

        Blogpost bp = findBlogpostByAddress(address);
        if (bp == null) {
            long CRC = computeCRC.computeChecksum(content);

            Blogpost aux = new Blogpost();
            aux.setPageAddress(address);
            aux.setBlogContent(content);
            aux.setBlogDate(date);
            aux.setProcessed(0);
            aux.setCrc(CRC);
            aux.setTitle(title);
            aux.setDescription(description);
            aux.setIdDomain(domain);

            blogpostController.create(aux);
            return findBlogpostByAddress(address);
        } else {
            //throw new Exception("This blogpost already exists in the database.");
            blogpostController.edit(bp);
            return bp;
        }
    }

    public List<Blogpost> findBlogpostByDomain(String domName) {
        Domain domain = findDomainByName(domName);
        return blogpostController.findByDomainCategory(domain);
    }

    public String getBlogPost(Blogpost blogpost) {
        return blogpost.getBlogContent();
    }

    public ArrayList<String> getDocumentForTopic(String topic) {
        ArrayList<String> content = new ArrayList<>();
        List<Blogpost> posts;
        List<Domain> domains = findDomainByCategory(topic);
        for (Domain dom : domains) {
            posts = findBlogpostByDomain(dom.getDomainName());
            String aux = "";
            for (Blogpost bp : posts) {
                aux += bp.getBlogContent() + " ";
            }
            content.add(aux.trim());
        }
        return content;
    }

    public ArrayList<String> getXDocumentsForTopic(String topic, int size) {
        ArrayList<String> content = new ArrayList<>();
        List<Blogpost> posts;
        List<Domain> domains = findDomainByCategory(topic);
        System.out.println(topic + " - " + domains.size());
        int min = Math.min(size, domains.size());
        for (int i = 0; i < min; i++) {
            posts = findBlogpostByDomain(domains.get(i).getDomainName());
            String aux = "";
            for (Blogpost bp : posts) {
                aux += bp.getBlogContent() + " ";
            }
            content.add(aux.trim());
        }
        return content;
    }

    public ArrayList<String> getXDocumentsofEachTopic(int size) {
        List<Category> categories = categoryController.findCategoryEntities();
        ArrayList<String> content = new ArrayList<>();
        List<Blogpost> posts;
        for (Category topic : categories) {
            List<Domain> domains = findDomainByCategory(topic.getCategory());
            int min = Math.min(size, domains.size());
            for (int i = 0; i < min; i++) {
                posts = findBlogpostByDomain(domains.get(i).getDomainName());
                String aux = "";
                for (Blogpost bp : posts) {
                    aux += bp.getBlogContent() + " ";
                }
                content.add(aux.trim());
            }
        }
        return content;
    }

    public List<Blogpost> getAllBlogposts() {
        return blogpostController.findBlogpostEntities();
    }

    public ArrayList<String> getDocumentForDomainName(String domName) {
        ArrayList<String> content = new ArrayList<>();
        List<Blogpost> posts;
        Domain domain = findDomainByName(domName);
        posts = findBlogpostByDomain(domain.getDomainName());
        String aux = "";
        for (Blogpost bp : posts) {
            aux += bp.getBlogContent() + " ";
        }
        content.add(aux.trim());

        return content;
    }

    public ArrayList<String> getAllDocuments() {
        ArrayList<String> content = new ArrayList<>();
        List<Blogpost> posts = getAllBlogposts();
        String aux = "";
        for (Blogpost bp : posts) {
            aux += bp.getBlogContent() + " ";
        }
        content.add(aux.trim());

        return content;
    }

//------------------------------------------------------------------------
//-------------Rawdata related methods------------------------------------
    public Rawdata findRawdataByAddress(String address) {
        return rawdataController.findByPageAddress(address);
    }

    public void addRawdata(String address, String content,
            int level, String title,
            String description, Domain domain) {
    }
//------------------------------------------------------------------------

//-------------Stopwords related methods----------------------------------
    public Stopword findStopwordByStopword(String word) {
        return stopwordController.findByStopword(word);
    }

    public void addStopword(String word) {

        try {
            Stopword stopword = new Stopword();
            stopword.setStopWord(word);
            stopwordController.create(stopword);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
//------------------------------------------------------------------------
//-------------Category related methods-----------------------------------

    public Category findCategoryByName(String name) {
        return categoryController.findByCategory(name);
    }
//------------------------------------------------------------------------
}

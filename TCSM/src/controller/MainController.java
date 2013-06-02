/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Blogroll;
import model.Domain;
import model.Location;

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
    private HeaderJpaController headerController;
    private RawdataJpaController rawdataController;
    private EntityManagerFactory emf;

// private constructer    
    private MainController() {
        
        emf = Persistence.createEntityManagerFactory("Crawler1.0PU");
        locationController = new LocationJpaController(emf);
        domainController = new DomainJpaController(emf);
        blogrollController = new BlogrollJpaController(emf);
        blogpostController = new BlogpostJpaController(emf);
        headerController = new HeaderJpaController(emf);
        rawdataController = new RawdataJpaController(emf);
    }
//singleton pattern

    public static MainController getInstance() {
        if (singleton == null) {
            singleton = new MainController();
        }
        return singleton;
    }
//-----------Location related methods-----------------------------

    public Location getLocationBySuffix(String suffix) {
        return locationController.findBySuffix(suffix);
    }
    
    public void addLocation(String suffix, String location) throws Exception {
        Location loc = getLocationBySuffix(suffix);
        
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
    public Domain getDomainByName(String name) {
        return domainController.findByDomainName(name);
    }
    
    public int addDomain(String name, String suffix, String robots, int depth, Date activation) throws Exception {
        Domain dom = getDomainByName(name);
        Location loc = getLocationBySuffix(suffix);
        
        if (dom == null) {
            Domain aux = new Domain();
            aux.setDomainName(name);
            aux.setIdLocation(loc);
            aux.setRobots(robots);
            aux.setDepth(depth);
            aux.setActivation(activation);
            domainController.create(dom);
            
            return getDomainByName(name).getIdDomain();
        } else {
            // throw new Exception("This domain already exists in the database.");
            return -1;
        }
    }
//---------------------------------------------------------------------
//-------------Blogroll related methods-------------------------------- 

    public List<Blogroll> getBlogrollByIdDomain(Domain domain) {
        return blogrollController.findByIdDomain(domain);
    }
    
    public Blogroll getBlogrollByDomainAndBlog(Domain domain, String blog) {
        return blogrollController.findByDomainAndName(domain, blog);
    }
    
    public void addBlogroll(Domain domain, String blog) throws Exception {
        
        Blogroll br = getBlogrollByDomainAndBlog(domain, blog);
        
        if (br == null) {
            Blogroll aux = new Blogroll();
            aux.setBlog(blog);
            aux.setIdDomain(domain);
            Domain destination = getDomainByName(blog);
            if (destination != null) {
                aux.setIddestination(destination.getIdDomain());
            }
            blogrollController.create(aux);
            
        } else {
            throw new Exception("This blogroll already exists in the database.");
        }
        
    }
//---------------------------------------------------------------------
//-------------Blogpost related methods--------------------------------
//------------------------------------------------------------------------
//-------------Rawdata related methods------------------------------------
//------------------------------------------------------------------------
//-------------Headers related methods------------------------------------ 
//------------------------------------------------------------------------
}

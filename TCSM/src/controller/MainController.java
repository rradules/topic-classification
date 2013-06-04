/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import functions.ComputeCRC;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Blogpost;
import model.Blogroll;
import model.Domain;
import model.Location;
import model.Rawdata;

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
    private ComputeCRC computeCRC;

// private constructer    
    private MainController() {

        emf = Persistence.createEntityManagerFactory("Crawler1.0PU");
        locationController = new LocationJpaController(emf);
        domainController = new DomainJpaController(emf);
        blogrollController = new BlogrollJpaController(emf);
        blogpostController = new BlogpostJpaController(emf);
        headerController = new HeaderJpaController(emf);
        rawdataController = new RawdataJpaController(emf);

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
            return dom;
        }
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
            aux.setProcessed(1);
            aux.setCrc(CRC);
            aux.setTitle(title);
            aux.setDescription(description);
            aux.setIdDomain(domain);

            blogpostController.create(aux);
            return findBlogpostByAddress(address);
        } else {
            //throw new Exception("This blogpost already exists in the database.");
            return bp;
        }
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
//-------------Headers related methods------------------------------------ 
//------------------------------------------------------------------------
}

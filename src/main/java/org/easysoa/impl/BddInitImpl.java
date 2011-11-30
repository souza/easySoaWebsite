/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easysoa.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.persistence.EntityManager;

import org.easysoa.jpa.Provider;
import org.easysoa.model.Town;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author dirix
 */
public class BddInitImpl implements Runnable {

    @Reference
    public Provider<EntityManager> db;

    public void initTowns() {
        EntityManager em = db.get();
        try {
        	boolean canPersist = false;
            String filePath = "c:/Users/Adel/Documents/worldcitiespop.txt";
            int index = 0;
            FileReader fr = new FileReader(new File(filePath));
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (index != 0) {
                    String[] datas = line.split(",");
                    if(datas.length == 7 && datas[2].equals("Bro Lâm Peh")){
                    	canPersist = true;
                    }
                    if (index % 3000 == 1) {
                            em.getTransaction().begin();
                        }
                    if (datas.length == 7 && !datas[0].equals("fr") && canPersist) {
                        Town town = new Town(datas[0], datas[2], Double.parseDouble(datas[5]), Double.parseDouble(datas[6]));
                        em.persist(town);
                    }
                    if (index % 3000 == 0) {
                            em.getTransaction().commit();
                        }
                    
                    
                }
                index++;
            }
            fr.close();
            br.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.initTowns();
    }
}

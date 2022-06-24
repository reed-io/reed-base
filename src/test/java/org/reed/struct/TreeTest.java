/**
 * base/org.reed.struct/TreeTest.java
 */
package org.reed.struct;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.reed.utils.FileUtil;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author chenxiwen
 * @date 2017年10月24日下午3:19:58
 */
public class TreeTest {

    /**
     * @param args
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Node<String> a = new Node<String>("a");
        Node<String> x = new Node<String>("x");
        a.addChild(x);
//        Node<String> b = new Node<String>("b");
//        Node<String> c = new Node<String>("c");
//        Node<String> d = new Node<String>("d");
//        a.addChildren(b,c,d);
//        Node<String> e = new Node<String>("e");
//        Node<String> f = new Node<String>("f");
//        Node<String> g = new Node<String>("g");
//        b.addChildren(e,f,g);
//        Node<String> h = new Node<String>("h");
//        Node<String> i = new Node<String>("i");
//        Node<String> j = new Node<String>("j");
//        c.addChild(h);
//        c.addChild(i);
//        c.addChild(j);
        
//        Tree<String> tree = new Tree<String>(a);
//        System.out.println(tree.getDepth());
        
        
        //------------------------------------------------------------------
        String orgJson = FileUtil.Loader.loadFile("c:\\Develop\\test.txt");
        long t = System.currentTimeMillis();
        JSONObject jo = JSONObject.parseObject(orgJson);
        System.out.println("parseCost:"+(System.currentTimeMillis()-t));
        t = System.currentTimeMillis();
        String name = jo.getString("moduleId");
        JSONObject jsontree = jo.getJSONObject("funcTree");
        Node<Group> root = buildTree(jsontree.toJSONString());
        Tree<Group> ftree = new Tree<Group>(root);
        System.out.println(name+" depth: "+ftree.getDepth());
    }
    
    public static Node<Group> buildTree(String groupJson){
        JSONObject groupJ = JSONObject.parseObject(groupJson);
        if(!groupJ.containsKey("groupId") || !groupJ.containsKey("groupName")){
            return null;
        }
        String groupId = groupJ.getString("groupId");
        String groupName = groupJ.getString("groupName");
        Node<Group> node = new Node<Group>();
        Group group = new Group();
        group.setId(groupId);
        group.setName(groupName);
        if(groupJ.containsKey("funcList")){
            JSONArray funcArr = groupJ.getJSONArray("funcList");
            Set<Function> fset = new HashSet<Function>();
            for(Object obj: funcArr){
                JSONObject jobj = (JSONObject)obj;
                if(jobj.containsKey("functionId") && jobj.containsKey("functionName") &&
                        jobj.containsKey("funcStatus") && jobj.containsKey("functionDesc")){
                    Function f = new Function();
                    f.setDesc(jobj.getString("functionDesc"));
                    f.setId(jobj.getString("functionId"));
                    f.setName(jobj.getString("functionName"));
                    f.setStatus(jobj.getString("funcStatus"));
                    fset.add(f);
                }
            }
            group.setFunctions(fset);
        }
        if(groupJ.containsKey("subGroup")){
            JSONArray groupArr = groupJ.getJSONArray("subGroup");
            Set<Group> gset = new HashSet<Group>();
            for(Object  obj : groupArr){
                Node<Group> n = buildTree(obj.toString());
                node.addChild(n);
//                gset.add(g);
            }
//            group.setSubGroups(gset);
        }
        node.setData(group);
        return node;
    }
    
    public static Group build(String groupJson){
        JSONObject groupJ = JSONObject.parseObject(groupJson);
        if(!groupJ.containsKey("groupId") || !groupJ.containsKey("groupName")){
            return null;
        }
        String groupId = groupJ.getString("groupId");
        String groupName = groupJ.getString("groupName");
        Group group = new Group();
        group.setId(groupId);
        group.setName(groupName);
        if(groupJ.containsKey("funcList")){
            JSONArray funcArr = groupJ.getJSONArray("funcList");
            Set<Function> fset = new HashSet<Function>();
            for(Object obj: funcArr){
                JSONObject jobj = (JSONObject)obj;
                if(jobj.containsKey("functionId") && jobj.containsKey("functionName") &&
                        jobj.containsKey("funcStatus") && jobj.containsKey("functionDesc")){
                    Function f = new Function();
                    f.setDesc(jobj.getString("functionDesc"));
                    f.setId(jobj.getString("functionId"));
                    f.setName(jobj.getString("functionName"));
                    f.setStatus(jobj.getString("funcStatus"));
                    fset.add(f);
                }
            }
            group.setFunctions(fset);
        }
        if(groupJ.containsKey("subGroup")){
            JSONArray groupArr = groupJ.getJSONArray("subGroup");
            Set<Group> gset = new HashSet<Group>();
            for(Object  obj : groupArr){
                Group g = build(obj.toString());
                gset.add(g);
            }
            group.setSubGroups(gset);
        }
        
        return group;
    }

}

class Group{
    private String id;
    private String name;
    private Set<Function> functions;
    private Set<Group> subGroups;
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the functions
     */
    public Set<Function> getFunctions() {
        return functions;
    }
    /**
     * @param functions the functions to set
     */
    public void setFunctions(Set<Function> functions) {
        this.functions = functions;
    }
    /**
     * @return the subGroups
     */
    public Set<Group> getSubGroups() {
        return subGroups;
    }
    /**
     * @param subGroups the subGroups to set
     */
    public void setSubGroups(Set<Group> subGroups) {
        this.subGroups = subGroups;
    }
    
    
}

class Function{
    private String id;
    private String name;
    private String desc;
    private String status;
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
}


class Org{
    private String orgId;
    private String orgName;
    
    
    
    /**
     * @param orgId
     * @param orgName
     */
    public Org(String orgId, String orgName) {
        super();
        this.orgId = orgId;
        this.orgName = orgName;
    }
    /**
     * @return the orgId
     */
    public String getOrgId() {
        return orgId;
    }
    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }
    /**
     * @param orgName the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    
    
}

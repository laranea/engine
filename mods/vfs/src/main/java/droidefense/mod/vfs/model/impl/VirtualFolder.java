package droidefense.mod.vfs.model.impl;

import droidefense.mod.vfs.model.base.IVirtualNode;
import droidefense.mod.vfs.model.base.VirtualNode;
import droidefense.mod.vfs.model.base.VirtualNodeType;

import java.util.ArrayList;

/**
 * Created by .local on 08/10/2016.
 */
public class VirtualFolder extends VirtualNode {

    private ArrayList<IVirtualNode> itemsInside;
    private int itemsInsideSize;

    public static VirtualFolder createFolder(VirtualFolder parentFolder, String name){
        //1 check if that virtual folder already exists on parentFolder
        VirtualFolder item = parentFolder.getFolder(name);
        if(item==null){
            return new VirtualFolder(parentFolder, name);
        }
        return item;
    }

    public static VirtualFolder createFolder(String name){
        //TODO check if this new virtual folder does not already exist
        return new VirtualFolder(name);
    }

    private VirtualFolder getFolder(String name) {
        ArrayList<VirtualFolder> list = getFolderList();
        for(int i=0; i<list.size(); i++){
            VirtualFolder item = list.get(i);
            if(item.getName().equals(name))
                return item;
        }
        return null;
    }

    private ArrayList<VirtualFolder> getFolderList() {
        ArrayList<VirtualFolder> list = new ArrayList<>();
        ArrayList<IVirtualNode> content = getItemList();
        for(int i=0; i<content.size(); i++){
            IVirtualNode item = content.get(i);
            if(item.getType()== VirtualNodeType.FOLDER)
                list.add((VirtualFolder) item);
        }
        return list;
    }

    private ArrayList<VirtualFile> getFileList() {
        ArrayList<VirtualFile> list = new ArrayList<>();
        ArrayList<IVirtualNode> content = getItemList();
        for(int i=0; i<content.size(); i++){
            IVirtualNode item = content.get(i);
            if(item.getType()== VirtualNodeType.FILE)
                list.add((VirtualFile) item);
        }
        return list;
    }

    private VirtualFolder(VirtualFolder parentFolder, String name) {
        super(parentFolder, name);
        itemsInside = new ArrayList<>();
        parentFolder.addAsFolderFile(this);
        parentFolder.setVirtualFoldersInside(parentFolder.getVirtualFoldersInside()+1);
        itemsInsideSize = 0;
    }

    private VirtualFolder(String name) {
        super(name);
        if(parentNode!=null){
            parentNode.setVirtualFoldersInside(parentNode.getVirtualFoldersInside()+1);
        }
        else{
            this.setVirtualFoldersInside(1);
        }
        itemsInside = new ArrayList<>();
        itemsInsideSize = 0;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    @Override
    public String toString() {
        String sb = "VirtualNode{" + "parentNode=" + parentNode +
                ", name='" + name + '\'' +
                '}';
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VirtualNode that = (VirtualNode) o;

        if (parentNode != null ? !parentNode.equals(that.getParentNode()) : that.getParentNode() != null) return false;
        return (name != null) ? name.equals(that.getName()) : (that.getName() == null);

    }

    @Override
    public int hashCode() {
        int result = parentNode != null ? parentNode.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    /**
     * This is just an estimation. Precision depends on the complexity of estimatedInMemorySize() method
     *
     * @return an estimation of the size in memory of this object
     */
    @Override
    public long estimatedInMemorySize() {
        return NEW_CLASS_ALLOCATION_HEAP_SIZE + itemsInsideSize;
    }

    @Override
    public int getItemsInside() {
        //return folder itselft plus all items inside it
        return 1 + itemsInside.size();
    }

    public void addAsFolderFile(IVirtualNode virtualFile) {
        if (virtualFile != null) {
            itemsInside.add(virtualFile);
            itemsInsideSize += virtualFile.estimatedInMemorySize();
        }
    }

    public void updateItemsInsideSize(int size) {
        this.itemsInsideSize += size;
    }

    @Override
    public VirtualNodeType getType() {
        return VirtualNodeType.FOLDER;
    }

    @Override
    public byte[] getContent() {
        return null;
    }

    @Override
    public IVirtualNode getItem(String name) {
        ArrayList<VirtualFile> list = getFileList();
        for(int i=0; i<list.size();i++){
            VirtualFile item = list.get(i);
            if(item.getName().equals(name))
                return item;
        }
        return null;
    }

    public ArrayList<IVirtualNode> getItemList() {
        return itemsInside;
    }

    public VirtualFile getFile(String name) {
        ArrayList<VirtualFile> list = getFileList();
        for(int i=0; i<list.size(); i++){
            VirtualFile item = list.get(i);
            if(item.getName().equals(name))
                return item;
        }
        return null;
    }
}
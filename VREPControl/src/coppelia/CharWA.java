// PUT_VREP_REMOTEAPI_COPYRIGHT_NOTICE_HERE

package coppelia;

public class CharWA
{
    char[] w;

    public CharWA(int i)
    {
        w = new char[i];
    }

    public void initArray(int i)
    {
        w = new char[i];
    }

    public void setArray(char[] input)
    {
    	w = input; 
    }
    
    public char[] getArray()
    {
        return w;
    }

    public int getLength()
    {
        return w.length;
    }

    public char[] getNewArray(int i)
    {
        w = new char[i];
        return w;
    }
}
import java.io.*;
import java.util.*;
import java.time.*;
class USER
  { 
    public static int currentuid=1001;

	public int userID;
    public String username=null;
	public USER next;
	public USER(String n) 
    {
	    userID=currentuid++;
        username=n;
        next=null;
	}
	public void displayuserdetails()
    {
        System.out.println("\nUserID="+userID+"\nUSERNAME="+username);
    }
  }

class USERLIST
{
    USER first;

    public USERLIST()
    {
        first=null;
    }
    public void registerUser(String uname)
    {
        USER newuser=new USER(uname);
        
        newuser.next=first;
        first=newuser;   
    }
    public boolean isempty()
    {
        return (first==null);
    }
    public USER getuserbyid(int i1)
    {
        USER curr = first;
        while (curr!=null)
        {
            if (curr.userID==i1)
            break;
            curr=curr.next;
        }
        return curr;
    }
}
class ITEM
{
        public static int currentiid=01;
        public static int currentsid=5001;
    
        public int itemId;
        public String name;
        public String description;
        public double startingPrice;
        public double currentBid;
        public int sellerid;
        public String sellername;
        public USER highestBidder;
        public ITEM next;
        public ITEM(String n,String d,double s,String se)
        {
            
            itemId=currentiid++;
            name=n;
            description=d;
            startingPrice=s;
            currentBid=s;
            sellerid=currentsid++;
            sellername=se;
            highestBidder=null;
            next=null;
        }
        public void displayitemdetails()
        {
            System.out.println("\nITEM ID="+itemId+"\nITEM NAME="+name+"\n"+"DESCRIPTION="+description+
            "\nSELLER="+sellername+"\nStarting Price="+startingPrice+"\nCurrent Bid="+currentBid+"\nHighest Bidder=" +(highestBidder != null ? highestBidder.username : "No bids yet"));
        }
}
        

class ITEMLIST
{
    ITEM first;
    public ITEMLIST()
    {
        first=null;
    }
    public void registerItem(String iname,String desc,double s1,String se1)
    {
        ITEM newitem=new ITEM(iname,desc,s1,se1);
        newitem.next=first;
        first=newitem;   
    }
    public boolean isempty()
    {
        return (first==null);
    }
    public void displayitemlist()
    {
        ITEM curr=first;
        while (curr!=null)
        {
            curr.displayitemdetails();
            curr=curr.next;
        }
        if (first==null)
        {
            System.out.println("No items yet!");
        }
    }
    public ITEM getitembyid(int i2)
    {
        ITEM curr = first;
        while (curr!=null)
        {
            if (curr.itemId==i2)
            break;
            curr=curr.next;
        }
        return curr;
    }
    public void deleteitem(int iid)
  {
     ITEM current=first;
     ITEM previous =null;
     while(current.itemId!=iid)
	{
	  if(current.next==null)
          return;
	  else
	      {
		previous=current;
		current=current.next;
	     }
    }
    
    if (current==first)
		first=first.next;
    else
		previous.next=current.next;
    System.out.println("Item removed from itemlist.");
    
  }


}
class BID
{
        public USER bidder;
        public double amount;
        public ITEM item;
        public Instant time;
        public BID next;
        public BID(USER b,double a,ITEM i)
        {
            bidder = b;
            amount= a;
            item= i;
            time=Instant.now();
            next=null;
        }
        public void displaybiddetails()
        {
            System.out.println("ITEM: "+"\n"+item.itemId+"\n"+item.name+"\n"+"BIDDER: "+"\n"+bidder.userID+"\n"+bidder.username+"\n"+"AMOUNT: "+amount+"\n"+"TIMESTAMP: "+time);
        }
}       
class BidPQueue
{
    public BID first;
    public BidPQueue()
    {
        first=null;
    }
    public boolean isempty()
    {
        return (first==null);
    }
    public void addbid(BID nl)
    {
        if (isempty() || nl.amount>first.amount)
        {
           nl.next=first;
           first=nl; 
        }
        else 
        {
            BID curr=first;
            BID prev=null;
            while (curr!=null &&  (nl.amount<curr.amount ))
            {
                    prev=curr;
                    curr=curr.next;
                
            }
           
            nl.next = curr;
            if (prev == null) {
                first = nl;
            } else {
                prev.next = nl;
            }
        }
        
    }
    public BID peek(ITEM it2)
    {
        BID curr = first;
        BID H_Bid = null;
        while (curr!=null)
        {
            if (curr.item==it2)
            {
                H_Bid= curr;
                break;
            }
            curr=curr.next;
        }
        return H_Bid;
    }
    
}
class BidManager
{
    private BidPQueue allbids ;
    public BidManager()
    {
        allbids= new BidPQueue();
    }
    public void placeBid(USER u,double a,ITEM item)
        {
          if (a<item.startingPrice || a==item.currentBid)
          {
          System.out.println("The bid amount should be greater than current bid");
          return;
          }
          BID newbid = new BID(u, a, item);
          allbids.addbid(newbid);
          BID highestBid = allbids.peek(item);
          
          item.highestBidder=highestBid.bidder;
          item.currentBid=highestBid.amount;
          System.out.println("BID placed successfully!");
        }
    public BidPQueue getBidsByItem(ITEM i3)
     {
        BidPQueue matchingBids = new BidPQueue(); 
        BID curr = allbids.first;
        while (curr != null)
        {
            if (curr.item.itemId==i3.itemId)
            {
               matchingBids.addbid(new BID(curr.bidder, curr.amount, curr.item));
            }
            curr = curr.next;
        }
        return matchingBids;
    }
    public void display(BidPQueue b1)
    {
        BID curr = b1.first;
        while (curr != null)
         {
            curr.displaybiddetails();
            curr = curr.next;
        }
    }
    
    public void endbidding(ITEM it1)
    {   
        USER winner = it1.highestBidder;
        if (winner == null) {
            System.out.println("No bids placed for this item.");  
            return;
        }
        System.out.println("Auction ending for "+it1.name+"....."+"\n"+"DETAILS...");
        it1.displayitemdetails();
        System.out.println("Auction won by: "+winner.userID+"\n"+winner.username+"\n"+" Sold at: "+it1.currentBid);
    }
}
public class AUCTION
{   
    public static void main(String []str)
    {

     USERLIST users = new USERLIST();
     ITEMLIST items = new ITEMLIST();
     BidManager bidlist = new BidManager();
    Scanner ob = new Scanner(System.in);
     int ch,k=1;
     while (k==1)
     {
     System.out.println("\n"+"OPTIONS"+"\n"+"*******************************"+"\n"+"1.Register user"+"\n"+" 2.Register item"+"\n"
     +"3.View items"+"\n"+"4.View bids on given item"+"\n"+"5.Place Bid"+"\n"+"6.End Auction(for seller)"+"\n"+"7.Exit"+"\nEnter your choice: ");
    ch=ob.nextInt();
    switch(ch)
    {
        case 1: ob.nextLine();
                System.out.println("Enter username: ");
                String UserName=ob.nextLine();
                users.registerUser(UserName);
                System.out.println("User registered successfully.");
                users.first.displayuserdetails();
                break;
        case 2: ob.nextLine();
                System.out.println("Enter item name: ");
                String ItemName=ob.nextLine();
                System.out.println("Enter item description: ");
                String description = ob.nextLine();
                System.out.println("Enter starting price: ") ;
                double Startingprice = ob.nextDouble();
                ob.nextLine(); 
                System.out.println("Enter seller name: ");
                String sellername= ob.nextLine();
                items.registerItem(ItemName, description, Startingprice,sellername);
                System.out.println("Item registered successfully.");
                break;
        case 3: items.displayitemlist();
                break;
        
        case 4:System.out.println("Enter the itemid whose bids are to be displayed: ");
                int id1=ob.nextInt();
                ITEM item1=items.getitembyid(id1);
                if (item1==null)
                {
                    System.out.println("invalid ID!");
                }
                else
                {
                BidPQueue b=bidlist.getBidsByItem(item1);
                    if (b.first==null)
                    {
                        System.out.println("No Bids yet.");
                    }
                    else
                    bidlist.display(b);
                }
                break;
        case 5: System.out.print("Enter your user ID: ");
                int userId = ob.nextInt();
                USER bidder = users.getuserbyid(userId);
                if (bidder==null){
                System.out.println("Invalid userID,please register first!");
                break;
                 }
                System.out.println("Enter the item ID: ");
                int id2=ob.nextInt();
                ITEM item2=items.getitembyid(id2);
                if (item2 == null) {  
                    System.out.println("Invalid item ID!");
                    break;
                }
                System.out.println("Enter your bid amount: ");
                double a1=ob.nextDouble();
                bidlist.placeBid(bidder,a1,item2);
                break;     
        case 6: while (true)
                { 
                    System.out.println("Enter Item ID: ");
                    int id3= ob.nextInt();
                    ITEM item3=items.getitembyid(id3);
                    if (item3 == null) 
                    {
                    System.out.println("Invalid Item ID or Item not found!");
                    System.out.println("Please try again!");
                    continue;
                    }
                    System.out.println("Are you sure you want to remove item "+item3.name+"?(y/n)");
                    ob.nextLine();
                    String confirm = ob.nextLine();
                    if (confirm.equals("y"))
                    {
                    bidlist.endbidding(item3);
                    items.deleteitem(id3);
                    break;
                    }
                    else{
                        System.out.println("Item deletion cancelled!");
                        break;
                    }
                }
                break;
        case 7: k=0;
                break;
        default:System.out.println("Invalid choice!");
     }           
    }
    ob.close();
}
}
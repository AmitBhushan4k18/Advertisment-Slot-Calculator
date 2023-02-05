package com.servlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SlotCalculator extends HttpServlet {
  @Override
protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	// get PrintWriter
      PrintWriter pw = res.getWriter();
   // set res type
      res.setContentType("text/html");
      //String filename = req.getParameter("filename");
      String quantity = req.getParameter("quantity");
      String quantity1 = req.getParameter("quantity1");
      
      int requiredTrp=Integer.parseInt(quantity1);
      int Timeslot=Integer.parseInt(quantity);
      
      Scanner fscanner = new Scanner (new File("D:\\trial.csv"));
      ArrayList<String> DataArray = new ArrayList<String>();
      while(fscanner.hasNext()) {
          DataArray.add(fscanner.nextLine());
      }
      fscanner.close();

      SlotCalculator Finals = new SlotCalculator();
      int Size  = DataArray.size();
      //int Timeslot = Integer.parseInt(DataArray.get(0));
      int[] Time = new int[Size + 1];
      int[] Cost = new int[Size + 1];
      String [] Brand= new String [Size + 1];
      int [] currentTrp = new int[Size + 1];
      for(int i = 0; i < DataArray.size(); i++)
      {
          String[] split = DataArray.get(i).split(",");
          currentTrp[i] = Integer.parseInt(split[3]);
          if (requiredTrp <=currentTrp[i]){
              Brand[i] = (split[0]);
              Time[i] = Integer.parseInt(split[1]);
              Cost[i] = Integer.parseInt(split[2]);
          }
          else{
              i++;
          }
      }
      
      
      Finals.solve(Brand,Time, Cost, Timeslot, Size,currentTrp);
}
  public void solve(String [] Brand,int[] Time, int[] Cost, int Timeslot, int size,int [] currentTrp) throws ServletException
  {
      // matrix which will store results
      int[][] Final = new int[size + 1][Timeslot + 1];
      // Fill in each row of matrix
      for (int i = 0; i <= size; i++)
      {
          // comparing each weight one by one
          for (int w = 0; w <= Timeslot; w++)
          {
              if (i == 0 || w == 0)
                  Final[i][w] = 0;
              else if (Time[i - 1] <= w)
                  Final[i][w] = Math.max(Cost[i - 1] +Final[i - 1][w - Time[i - 1]], Final[i - 1][w]);
              else
                  Final[i][w] = Final[i - 1][w];
          }
          
      }
      // Result of the knapsack algorithm
      
      //PrintWriter pw = res.getWriter();
      int Revenue = Final[size][Timeslot];
      int w = Timeslot;
      System.out.println("Total revenue generated1: "+ Revenue);
      System.out.println("BRAND " + " TIME " + " COST "+" TRP ");
      for (int i = size; i > 0 && Revenue > 0; i--) {
          if (Revenue == Final[i - 1][w])
              continue;
          else {

        	  System.out.println( Brand[i - 1] +("   ")+ Time[i - 1]+("   ")+  Cost[i - 1]+("   ")+ currentTrp[i - 1] );
              // value is deducted as weight is included in knapsack
              Revenue = Revenue - Cost[i - 1];
              w = w - Time[i - 1];
          }
      }
      
  }
  @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req,res);
	}
}

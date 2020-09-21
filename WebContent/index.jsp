<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  	<link rel="icon" href="https://evelynlearning.com/wp-content/uploads/2018/09/cropped-favicon-32x32.png" type = "image/x-icon"> 
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Feedback Creator</title>
  <!-- bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
  <!-- bootstrap-end -->
</head>
 <body>
	
   <div class="container mt-5">
     <div class="row justify-content-center">
     
     	<div class="card" style="border-radius:8px;  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">
   	      <form action="ConnectionOnButtonClick" method="post">
            <div style="background:#a64ab8;border-top-left-radius:8px;border-top-right-radius:8px;" class="mb-3  py-2">
            	<h4 class="text-center" style="color:#ffffff;">Feedback Creator</h4>
		   	</div>
		   	${message}
		    <div class="form-row row w-100 justify-content-center">
		        <div class="col-lg-5">
		            <label for="FromDate" class="mr-2 col-form-label-sm">Submission Date : &nbsp;&nbsp;&nbsp;&nbsp;</label>
		        </div>
		        <div class="col-lg-6">
		        	<input type="date" class="form-control form-control-sm" name="date" required/>
		    	</div>
	    	</div>
		    <div class="form-row row w-100 justify-content-center" style="margin-top:-5px;">
		        <div class="col-lg-5">
		            <label for="department" class="mr-2 col-form-label-sm">Department : &nbsp;&nbsp;&nbsp;&nbsp;</label>
		        </div>
		        <div class="col-lg-6">
<!-- 		            <input type="text" class="form-control form-control-sm" name="department" required/> -->
						<select class="custom-select" name="department">
						  <option selected>Select Department</option>
						  <option value="Accounting">Accounting</option>
						  <option value="Biology">Biology</option>
						  <option value="Business">Business</option>
						  <option value="Chemistry">Chemistry</option>
						  <option value="Computer Science">Computer Science</option>
						  <option value="Economics">Economics</option>
						  <option value="Electrical Engineering">Electrical Engineering</option>
						  <option value="Health">Health</option>
						  <option value="Math">Math</option>
						  <option value="Psychology">Psychology</option>
						  <option value="Statistics">Statistics</option>
						</select>
		        </div>
	        </div>

		    <div class="form-row row w-100 mt-4 justify-content-center">
		        <div class="col-lg-5">
		            <label for="spreadsheet" class="mr-2 col-form-label-sm">SpreadSheet URL : &nbsp;&nbsp;&nbsp;&nbsp;</label>
		        </div>
		        <div class="col-lg-6">
		            <input type="url" class="form-control form-control-sm" name="sheetURL" required/>
		        </div>
	        </div>
	        
	        <div class="form-row row w-100 justify-content-center">
		        <div class="col-lg-5">
		            <label for="submission" class="mr-2 col-form-label-sm">SubmissionSheet URL : &nbsp;&nbsp;&nbsp;&nbsp;</label>
		        </div>
		        <div class="col-lg-6">
		            <input type="url" class="form-control form-control-sm" name="submissionURL" required/>
		        </div>
	        </div>
	        
	        <hr width=90%/>
<!-- -----------------------------------------email and password----------------------------------------------------------------------- -->
   	        <div class="form-row row w-100 justify-content-center">
		        <div class="col-lg-5 ">
		            <label for="email" class="mr-2 col-form-label-sm">Email ID :&nbsp;&nbsp;&nbsp;&nbsp;</label>
		        </div>
		        <div class="col-lg-6">
		            <input type="email" class="form-control form-control-sm" name="email" required/>
		        </div>
	        </div>
	        
     	    <div class="form-row row w-100 justify-content-center">
		        <div class="col-lg-5 ">
		            <label for="password" class="mr-2 col-form-label-sm">Password :&nbsp;&nbsp;&nbsp;&nbsp;</label>
		        </div>
		        <div class="col-lg-6">
		            <input type="password" class="form-control form-control-sm" name="password" required/>
		        </div>
	        </div>
<!-- -----------------------------------------email and password end----------------------------------------------------------------------- -->
	        
	        <div class="col-lg-12 d-flex justify-content-center mt-3 mb-3">
	            <button type="submit" class="btn btn-md" style="background: #a64ab8;color:white;">Submit</button>
	        </div>
	        	        
	       	<a style="float:right;color:#839bff;cursor:pointer;" class="mr-3 mb-1" target="_blank" href="https://docs.google.com/forms/d/e/1FAIpQLSerUTecz-ncfiw3XhSfo8LgK2rZZU60t6pLYovfRk_7spiw-A/viewform">Contact Us</a>
		</form>      	
      	</div>
      </div>
    </div>
</body>
</html>
<?php 
error_reporting(E_ALL);
ini_set('display_errors', 1);
?>
<!doctype html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>LED Display Message Update</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="styles.css" rel="stylesheet" type="text/css">
    </head>
    <body>

        <?php
        // define variables and set to empty values
        $message = "";
		$textdec = "";

        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            $message = test_input($_POST["message"]);
			$textdec = test_input($_POST["textdec"]);
        }

        function test_input($data) {
            $data = trim($data);
            $data = stripslashes($data);
            $data = htmlspecialchars($data);
            return $data;
        }
        ?>
        <!------jQuery
        <script type="text/javascript">
$(function () {
    $('#button').on('click', function () {
        var text = $('#text');
        text.val(text.val() + ' after clicking');    
    });
});
</script>--->
<!------Javascript
<script type="text/javascript">
document.getElementById('button').addEventListener('click', function () {
    var text = document.getElementById('text');
    text.value += ' after clicking';
});
</script>--->
<!----Javascript2--->
<script language="javascript" type="text/javascript">

function blinkONtext() {
	var newtext = document.form1.message.value;
	document.form1.message.value += "~|^";
}
function blinkOFFtext() {
	var newtext = document.form1.message.value;
	document.form1.message.value += "^|~";
}
</script>
        <div id="example">
            <p class="h1">LED Display Message Update</p>
            <p class="content">The LED Display can be updated by putting the new message in the textbox and selecting Scroll or None. Blink can not be used with Scroll. Note, there is  a 15 second delay after hitting the "Send to Display" button.</p>
            <p class="content">
            <form method="post" action= "<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" name="form1">Message: <input type="text" name="message" value="" width="600px">
            </p>
            <input type="button" id="blinkON" value="Blink ON" onClick="blinkONtext()">&nbsp;<input type="button" id="blinkOFF" value="Blink OFF" onClick="blinkOFFtext()">
            <p>
            Sign options:
            </p>
            <p>
            <input type="radio" name="textdec"
            <?php if (isset($textdec) && $textdec=="SCROLL") echo "checked";?>
value="SCROLL">Scrolling Text
			</p>
            <p>
			<input type="radio" name="textdec"
            <?php if (isset($textdec) && $textdec=="NONE") echo "checked";?>
value="NONE">None
			</p>
                <p class="h1"><input type="submit" name="submit" value="Send to Display"></p>
            </form>
        </p>
        <?php
        $DBServer = 'localhost';
        $DBUser = 'client';
        $DBPass = 'admin';
        $DBName = 'messagedb';
        $conn = new mysqli($DBServer, $DBUser, $DBPass, $DBName);
        $error = 0;
        $isupdated2 = 0;
		// check connection
        if ($conn->connect_error) {
            trigger_error('Database connection failed: ' . $conn->connect_error, E_USER_ERROR);
        }

        //Select error from t
		$sql = 'SELECT error FROM t';
		$rs = $conn->query($sql);
		
		if ($rs->num_rows > 0) {
    		// output data of each row
			
	    	while($row = $rs->fetch_assoc()) {
        		$GLOBALS['error'] = $row["error"];
    		}
		} 
		
		$sql = 'SELECT * FROM t';
        $rs = $conn->query($sql);
        if ($rs === false) {
            trigger_error('Wrong SQL: ' . $sql . ' Error: ' . $conn->error, E_USER_ERROR);
        } else {
//            $rows_returned2 = $rs->num_rows;
//            $rs->data_seek(0);
//            while ($row = $rs->fetch_row()) {
//                echo 'The old message was: ' . $row[0] . '<br>';
//            }

            $v1 = $conn->real_escape_string($message);
			$scrollON = 0;
			if ($textdec=="NONE") {
				$scrollON = 0;
			}
			else if ($textdec=="SCROLL") {
				$scrollON = 1;
			}
			
			//$scrollON = "'" . $conn->real_escape_string($scroll) . "'";
			
			//$blinkON = "'" . $conn->real_escape_string($blink) . "'";         
            
            $sql = "UPDATE t SET message='$v1', isupdated='FALSE', scrollon='$scrollON'";
//            echo "<br />$sql<br />";

            if ($conn->query($sql) === false) {
                trigger_error('Wrong SQL: ' . $sql . ' Error: ' . $conn->error, E_USER_ERROR);
            } else if ($message !== "") {
                $affected_rows = $conn->affected_rows;
//			exec("java -jar RS232_Example.jar", $output);
				sleep(15);
				
				//Select error from t
				$sql = 'SELECT isupdated FROM t';
				$rs = $conn->query($sql);
		
				if ($rs->num_rows > 0) {
    			// output data of each row
			
	    		while($row = $rs->fetch_assoc()) {
        			$GLOBALS['isupdated2'] = $row["isupdated"];
    			}
		}
				if ( ($GLOBALS['error'] === "0") && ($GLOBALS['isupdated2'] === "1") ) {
                	echo "<script type='text/javascript'>alert('The message, \"$message\", was sent successfully to the display.');</script>";
				}
				else {
					echo "<script type='text/javascript'>alert('There is a communications error. \\nThe sign will be updated after the Java back-end\\nis restarted and the connections are correct.');</script>";
				}
            }
        }
        ?>
        
    </div>
</body>
</html>
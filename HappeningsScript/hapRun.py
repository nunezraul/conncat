import sys
import imaplib
import email
import datetime
import re 
import fnmatch

file = open("happenings.xml", "w")
#file.write(" ") <- write to file
date = (datetime.date.today())
mail = imaplib.IMAP4_SSL('imap.gmail.com')
mail.login('some-email@email.com', 'thisisnotpassword...')
mail.list()
mail.select("INBOX")
result, data = mail.uid('search', None, "ALL")

status, response = mail.status('INBOX', "(UNSEEN)")
unreadcount = int(response[0].split()[2].strip(').,'))
#print unreadcount

status, email_ids = mail.search(None, '(UNSEEN)')
#print email_ids
#count =0

#print mail.list()
result, data = mail.uid('search', None, "ALL")
keyword_list = ['Subject:', 'Categories:', 'Event Start:', 'Event End:', 'Event Location:', 'Description:']
if result == 'OK':
    for num in data[0].split():
        result, data = mail.uid('fetch', num, '(RFC822)')
        if result == 'OK':
            email_message = email.message_from_string(data[0][1]) 

            e_parse = email_message['Subject']
            if email_message['From'] == 'Justine Chuang <jchuang2@ucmerced.edu>':
                if email_message.is_multipart():
                    itera = iter(email_message.get_payload())
                    flag = 1
                    for payload in itera:
                        if flag == 1:
                            next(itera)
                        
                        e1_content = payload.get_payload()
                        #print type(e1_content)
                        #print e_content[143]
                        #print e1_content
                        e2_ = e1_content.replace("=\r\n", "")
                        e3_ = e2_.replace("&", "&amp;")
                        e4_ = e3_.replace("=92", "&#39;")
                        e5_ = e4_.replace("=94", "")
                        e6_ = e5_.replace("=96", "")
                        e7_ = e6_.replace("=93", "")                        
                        e8_ = e7_.replace("=97", "")
                        e9_ = e8_.replace("<", "&lt;")
                        e10_ = e9_.replace(">", "&gt; ")
                        e11_ = e10_.replace("@", "&#64;")
                        e12_ = e11_.replace('"', "&quot;")
                        e13_ = e12_.replace("'", "&#39;")

                        e_content = e13_.replace("?", "&#63;")
                        
                        file.write('<event>\n')
                        file.write('<link>Happenings</link>\n')
                        
                        for item in keyword_list:

                            if item == 'Subject:':
                                if 'Subject: ' in e_content: 
                                    start = e_content.index('Subject: ') + len('Subject: ')
                                    end = e_content.index('\n', start)
                                    sub = e_content[start:end]
                                    if ' - ' in sub:
                                        end2 = sub.index(' - ', 0)
                                        sub2 = sub[0:end2]
                                        file.write('<summary>'+ sub2 + '</summary>' + '\n')
                                        #print sub2
                                    else:
                                        #continue
                                        file.write('<summary>'+ sub + '</summary>' + '\n')
                                        #print sub

                                else:
                                    #continue
                                    file.write('<summary>N/A</summary>' + '\n')
                                    #print 'Subject: N/A'

                            elif item == 'Categories:':
                                if 'Categories:' in e_content:
                                    start = e_content.index('Categories: ')+len('Categories: ')
                                    end = e_content.index('Published:', start)
                                    cats = e_content[start:end]
                                    count = cats.count(',')
                                    cats_ = cats.split(', ', count)
                                    #file.write('Categories: '+ cats)
                                    for x in cats_:
                                        #z = cats[x]
                                        x = x.replace("\n", "")
                                        #file.write(y + '\n')
                                        file.write("<category> \n<value>")
                                        file.write(x)
                                        file.write("</value>\n</category>\n")
                                        #file.write("HELLO")

                                    #print cats
                                elif 'Category:' in e_content:
                                    start = e_content.index('Category: ')+len('Category: ')
                                    end = e_content.index('Published:', start)
                                    cat = e_content[start:end]
                                    file.write('<category> \n <value>\n' + cat + '</value> \n </category>\n')
                                    #file.write('Categories: '+ cat)
                                    #print cat

                                else:
                                    #continue
                                    file.write('Categories: N/A' + '\n')
                                    #print 'Categories: N/A'

                            elif item == 'Event Start:':
                                if 'Event Start:' in e_content:
                                    start = e_content.index('Event Start: ')+len('Event Start: ')
                                    end = start + 21
                                    e_st = e_content[start:end]
                                    #file.write('Event Start: '+ e_st)
                                    e_st_dt = e_st[0:10]
                                    e_st_dt = e_st_dt.replace("/", "")
                                    file.write('<start> <fourdigityear>'+e_st_dt[4]+e_st_dt[5]+e_st_dt[6]+e_st_dt[7]+ '</fourdigityear>' +'\n')
                                    file.write('<twodigitmonth>'+e_st_dt[0]+e_st_dt[1]+"</twodigitmonth>"+'\n')
                                    file.write('<twodigitday>'+e_st_dt[2]+e_st_dt[3]+'</twodigitday>'+'\n')


                                    if e_st[18] == 'A':
                                        #e_st[12:18]
                                        file.write('<twodigithour24>' + e_st[12:14] + '</twodigithour24>' + '\n')
                                        file.write('<twodigitminute>' + e_st[15:17] + '</twodigitminute>' + '\n')
                                        file.write('</start>' + '\n')

                                    elif e_st[18] == 'P':
                                        timcheck = e_st[12:14]
                                        timcheck = int(timcheck)
                                        timcheck=timcheck+12
                                        timcheck=str(timcheck)
                                        t_c = e_st[12:18]

                                        timee = t_c[:0] + timcheck[0] + t_c[0+1:]
                                        timee_ = timee[:1] + timcheck[1] + timee[2:]

                                        #file.write('Event Start Time: '+ timee_ + '\n')
                                        file.write('<twodigithour24>' + timee_[0] + timee_[1] + '</twodigithour24>' + '\n')
                                        file.write('<twodigitminute>'+timee_[3] + timee_[4] + '</twodigitminute>' + '\n')
                                        file.write('</start>' + '\n')

                                   # elif e_st[18] == 'P' & e_st[13] == 2:
                                        #continue

                                else:
                                    #continue
                                    file.write('<start>\n')
                                    file.write('<fourdigityear>N/A</fourdigityear>\n<twodigitmonth>N/A</twodigitmonth>\n<twodigitday>N/A</twodigitday>\n<twodigithour24>N/A</twodigithour24>\n<twodigitminute>N/A</twodigitminute>\n')
                                    file.write('</start>' + '\n')
                                    #file.write('Event Start Date: N/A' + '\n')
                                    #file.write('Event Start Time: N/A' + '\n')
                                    #print 'No Event Start Time'
                                    #Event Start: 22
       
                            elif item == 'Event End:':
                                if 'Event End:' in e_content:
                                    start = e_content.index('Event End: ')+len('Event End: ')
                                    end = start + 21
                                    e_en = e_content[start:end]
                                    #file.write('Event End: '+ e_en)
                                    e_en_dt = e_en[0:10]
                                    e_en_dt = e_en_dt.replace("/", "")
                                    #file.write('Event End Date: '+e_en_dt+'\n')
                                    file.write('<end> <fourdigityear>'+e_st_dt[4]+e_st_dt[5]+e_st_dt[6]+e_st_dt[7]+ '</fourdigityear>' +'\n')
                                    file.write('<twodigitmonth>'+e_st_dt[0]+e_st_dt[1]+"</twodigitmonth>"+'\n')
                                    file.write('<twodigitday>'+e_st_dt[2]+e_st_dt[3]+'</twodigitday>'+'\n')
                                    #print(e_en)
                                    if e_en[18]=='A':
                                        #file.write('Event End Time: ' + e_en[12:18] + '\n')
                                        file.write('<twodigithour24>' + e_en[12:14] + '</twodigithour24>' + '\n')
                                        file.write('<twodigitminute>' + e_en[15:17] + '</twodigitminute>' + '\n')
                                        file.write('</end>' + '\n')
                                    elif e_en[18] == 'P':
                                        timcheck = e_en[12:14]
                                        timcheck = int(timcheck)
                                        timcheck = timcheck +12
                                        timcheck = str(timcheck)
                                        t_c = e_en[12:18]
                                        timee = t_c[:0] + timcheck[0] + t_c[0+1:]
                                        timee_ = timee[:1] + timcheck[1] + timee[2:]
                                        #file.write('Event End Time: '+timee_+'\n')
                                        file.write('<twodigithour24>' + timee_[0] + timee_[1] + '</twodigithour24>' + '\n')
                                        file.write('<twodigitminute>'+timee_[3] + timee_[4] + '</twodigitminute>' + '\n')
                                        file.write('</end>' + '\n')
                                
                                else: 
                                    #continue
                                    file.write('<end>\n')
                                    file.write('<fourdigityear>N/A</fourdigityear>\n<twodigitmonth>N/A</twodigitmonth>\n<twodigitday>N/A</twodigitday>\n<twodigithour24>N/A</twodigithour24>\n<twodigitminute>N/A</twodigitminute>\n')
                                    file.write('</end>' + '\n')
                                    #file.write('Event End Date: N/A' + '\n')
                                    #file.write('Event End Time: N/A' + '\n')
                                    #print 'Event End: N/A'
                                    #Event End: 25

                            elif item == 'Event Location:':
                                if 'Event Location:' in e_content:
                                    start = e_content.index('Event Location: ') + len('Event Location: ')
                                    end = e_content.index('\n', start)
                                    loc = e_content[start:end]
                                    file.write('<address>' + loc + '</address>' + '\n')
                                    #print loc
                                    #print '\n'

                                else:
                                    #continue
                                    file.write('<address> N/A </address>' + '\n')
                                    #print 'No Location'
                                    #print '\n'
                            elif item == 'Description:':
                                if 'Event Location: ' in e_content:
                                    match = re.search(r"Event Location: ([^\n]*)", e_content)
                                    if match:
                                        cmatch = match.group(1)
                                    start = e_content.index(cmatch) + len(cmatch)
                                    end = e_content.index('Please visit the Portal Happenings', start)
                                    descr = e_content[start:end]
                                    descr_ = descr.replace("\n", "&#xA;")
                                    descr__ = descr_.replace("\r", "&#xA;")
                                    file.write('<description>'+ descr__ + '</description>' + '\n')
                                    #continue

                                else:
                                    start = e_content.index('change your E-mail Opt-In on the My Preferences page.') + len('change your E-mail Opt-In on the My Preferences page.')
                                    end = e_content.index('Please visit the Portal Happenings', start)
                                    descr = e_content[start:end]
                                    descr_ = descr.replace("\n", "&#xA;")
                                    descr__ = descr_.replace("\n", "&#xA;")
                                    file.write('<description>' + descr__ + '</description>' + '\n')

                file.write('</event>')
                file.write ("\n\n\n\n\n")

file.close()
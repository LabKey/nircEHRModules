package org.labkey.nirc_ehr.notification;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.labkey.api.data.Container;
import org.labkey.api.ldk.notification.NotificationService;
import org.labkey.api.security.User;
import org.labkey.api.security.UserPrincipal;
import org.labkey.api.util.MailHelper;

import jakarta.mail.Address;
import jakarta.mail.Message;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//a class to send notification message based on an animal event (Ex. death, birth, etc.) from a trigger script helper, essentially from a trigger script.
public class TriggerScriptNotification
{
    private static final Logger _log = LogManager.getLogger(TriggerScriptNotification.class);

    public static void sendMessage(String subject, String bodyHtml, Collection<UserPrincipal> recipients, Container container, User user)
    {
        try
        {
            Address from = NotificationService.get().getReturnEmail(container);
            if (from == null)
            {
                _log.warn("No return email set for EHR NotificationService.");
                return;
            }

            MailHelper.MultipartMessage msg = MailHelper.createMultipartMessage();
            msg.setFrom(from);
            msg.setSubject(subject);

            List<String> emails = new ArrayList<>();
            for (UserPrincipal u : recipients)
            {
                List<Address> addresses = NotificationService.get().getEmailsForPrincipal(u);
                if (addresses != null)
                {
                    for (Address a : addresses)
                    {
                        if (a.toString() != null)
                            emails.add(a.toString());
                    }
                }
            }

            if (emails.size() == 0)
            {
                _log.warn("No emails, unable to send EHR trigger script email");
                return;
            }

            msg.setRecipients(Message.RecipientType.TO, StringUtils.join(emails, ","));
            msg.setEncodedHtmlContent(bodyHtml);

            MailHelper.send(msg, user, container);
        }
        catch (Exception e)
        {
            _log.error("Unable to send email from EHR trigger script", e);
        }
    }
}

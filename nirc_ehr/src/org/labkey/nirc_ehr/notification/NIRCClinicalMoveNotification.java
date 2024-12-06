package org.labkey.nirc_ehr.notification;

import org.jetbrains.annotations.Nullable;
import org.labkey.api.data.Container;
import org.labkey.api.ehr.notification.AbstractEHRNotification;
import org.labkey.api.security.User;

import java.util.Date;

public class NIRCClinicalMoveNotification extends AbstractEHRNotification
{
    @Override
    public String getName()
    {
        return "Clinical Move Notification";
    }

    @Override
    public String getScheduleDescription()
    {
        return "Sent immediately when a housing transfer is recorded with the reason 'Veterinary Treatment'";
    }

    @Override
    public @Nullable String getMessageBodyHTML(Container c, User u)
    {
        return "";
    }

    @Override
    public String getDescription()
    {
        return "The report sends an alert whenever an animal has a housing transfer and the reason for the move is 'Veterinary Treatment'.";
    }

    @Override
    public String getEmailSubject(Container c)
    {
        return "Clinical Move Alert: " + getDateTimeFormat(c).format(new Date());
    }
}

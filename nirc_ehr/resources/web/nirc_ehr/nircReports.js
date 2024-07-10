Ext4.namespace('EHR.reports');

EHR.reports.clinicalHistoryPanelXtype = 'nirc_ehr-snapshotpanel';


EHR.reports.snapshot = function(panel, tab, showActionsBtn){
    if (tab.filters.subjects){
        renderSubjects(tab.filters.subjects, tab);
    }
    else
    {
        panel.resolveSubjectsFromHousing(tab, renderSubjects, this);
    }

    function renderSubjects(subjects, tab){
        var toAdd = [];
        if (!subjects.length){
            toAdd.push({
                html: 'No animals were found.',
                border: false
            });
        }
        else if (subjects.length < 10) {
            for (var i=0;i<subjects.length;i++){
                toAdd.push({
                    xtype: 'ldk-webpartpanel',
                    title: 'Overview: ' + subjects[i],
                    items: [{
                        xtype: 'nirc_ehr-snapshotpanel',
                        showExtendedInformation: true,
                        showActionsButton: !!showActionsBtn,
                        hrefTarget: '_blank',
                        border: false,
                        subjectId: subjects[i]
                    }]
                });

                toAdd.push({
                    border: false,
                    height: 20
                });

                toAdd.push(EHR.reports.renderWeightData(panel, tab, subjects[i]));
            }
        }
        else {
            toAdd.push({
                html: 'Because more than 10 subjects were selected, the condensed report is being shown.  Note that you can click the animal ID to open this same report in a different tab, showing that animal in more detail or click the link labeled \'Show Hx\'.',
                style: 'padding-bottom: 20px;',
                border: false
            });

            var filterArray = panel.getFilterArray(tab);
            var title = panel.getTitleSuffix();
            toAdd.push({
                xtype: 'ldk-querycmp',
                style: 'margin-bottom:20px;',
                queryConfig: {
                    title: 'Overview' + title,
                    schemaName: 'study',
                    queryName: 'demographics',
                    viewName: 'Snapshot',
                    filterArray: filterArray.removable.concat(filterArray.nonRemovable)
                }
            });
        }

        if (toAdd.length)
            tab.add(toAdd);
    }
};

EHR.reports.currentBlood = function(panel, tab){
    var filterArray = panel.getFilterArray(tab);
    var title = panel.getTitleSuffix();

    tab.add({
        html: 'This report summarizes the blood available for the animals below.  ' +
                '<br><br>If there have been recent blood draws for the animal, a graph will show the available blood over time.  On the graph, dots indicate dates when either blood was drawn or a previous blood draw fell off.  The horizontal lines indicate the maximum allowable blood that can be drawn on that date.',
        border: false,
        style: 'padding-bottom: 20px;'
    });

    tab.add({
        xtype: 'ldk-querycmp',
        style: 'margin-bottom: 10px;',
        queryConfig: panel.getQWPConfig({
            title: 'Summary',
            schemaName: 'study',
            queryName: 'Demographics',
            viewName: 'Blood Draws',
            filterArray: filterArray.removable.concat(filterArray.nonRemovable)
        })
    });

    var subjects = tab.filters.subjects || [];

    if (subjects.length){
        tab.add({
            xtype: 'nirc-bloodsummarypanel',
            subjects: subjects
        });
    }
    else
    {
        panel.resolveSubjectsFromHousing(tab, function(subjects, tab){
            tab.add({
                xtype: 'nirc-bloodsummarypanel',
                subjects: subjects
            });
        }, this);
    }
};

EHR.reports.clinicalHistory = function(panel, tab, showActionsBtn, includeAll){
    if (tab.filters.subjects){
        renderSubjects(tab.filters.subjects, tab);
    }
    else
    {
        panel.resolveSubjectsFromHousing(tab, renderSubjects, this);
    }

    function renderSubjects(subjects, tab){
        if (subjects.length > 10){
            tab.add({
                html: 'Because more than 10 subjects were selected, the condensed report is being shown.  Note that you can click the animal ID to open this same report in a different tab, showing that animal in more detail or click the link labeled \'Show Hx\'.',
                style: 'padding-bottom: 20px;',
                border: false
            });

            var filterArray = panel.getFilterArray(tab);
            var title = panel.getTitleSuffix();
            tab.add({
                xtype: 'ldk-querycmp',
                style: 'margin-bottom:20px;',
                queryConfig: {
                    title: 'Overview' + title,
                    schemaName: 'study',
                    queryName: 'demographics',
                    viewName: 'Snapshot',
                    filterArray: filterArray.removable.concat(filterArray.nonRemovable)
                }
            });

            return;
        }

        if (!subjects.length){
            tab.add({
                html: 'No animals were found.',
                border: false
            });

            return;
        }

        tab.addCls('ehr-snapshotsubpanel');

        var minDate = includeAll ? null : Ext4.Date.add(new Date(), Ext4.Date.YEAR, -30);
        var toAdd = [];
        Ext4.each(subjects, function(s){
            toAdd.push({
                html: '<span style="font-size: large;"><b>Animal: ' + Ext4.htmlEncode(s) + '</b></span>',
                style: 'padding-bottom: 20px;',
                border: false
            });

            toAdd.push({
                xtype: EHR.reports.clinicalHistoryPanelXtype,
                showActionsButton: !!showActionsBtn,
                hrefTarget: '_blank',
                border: false,
                subjectId: s
            });

            toAdd.push({
                html: '<b>Chronological History:</b><hr>',
                style: 'padding-top: 5px;',
                border: false
            });

            toAdd.push({
                xtype: 'ehr-clinicalhistorypanel',
                border: true,
                subjectId: s,
                autoLoadRecords: true,
                minDate: minDate,
                //maxGridHeight: 1000,
                hrefTarget: '_blank',
                style: 'margin-bottom: 20px;'
            });
        }, this);

        if (toAdd.length){
            tab.add(toAdd);
        }
    }
};

EHR.reports.FileRepository =  function(panel,tab) {
    if (tab.filters.subjects && tab.filters.subjects.length === 1) {
        var animalIds = tab.filters.subjects[0];
        renderFiles(animalIds,tab);
    }
    else if (tab.filters.subjects && tab.filters.subjects.length > 1) {
        tab.add({
            html:'Only one animal Id can be selected at a time for the file repository',
            border : false
        });
    }
    else {
        tab.add({
            html:'No animal files found.',
            border : false
        });
    }

    const folders = [
        "Misc Docs",
        "Clinical Docs"
    ];

    function renderNotFound(panel, animalFolder, reportHandler, id) {
        panel.add({
            xtype: 'ldk-webpartpanel',
            title: 'File Repository for ' + id,
            items: [
                {
                    xtype: 'label',
                    text: 'Not all file directories created for this animal. Click to create directories.  '
                },
                {
                    xtype: 'label',
                    html: '<br/><br/>'

                },
                {
                    xtype: 'button',
                    style: 'margin-left: 10px;',
                    border: true,
                    text: 'Create Directories',
                    handler: function () {
                        var createdCount = 0;

                        folders.forEach(function (folder) {
                            animalFolder.createDirectory({
                                path: "/@files/" + id + "/" + folder + "?createIntermediates=t&overwrite=f",
                                success: function () {
                                    console.log("created " + folder + " folder for " + id);
                                    createdCount++;
                                    if (createdCount === folders.length) {
                                        reportHandler(id);
                                    }
                                },
                                failure: function (error) {
                                    console.log("Failed to create " + folder + " folder" + error.status);
                                    alert("Failed to create " + folder + " folder" + error.status);
                                }
                            })
                        });
                        console.log("directories created for " + id);
                    }
                }]
        });
    }

    function renderFiles(subjects, tab)
    {
        // WebdavFileSystem depends on ExtJS3 so load it dynamically
        LABKEY.requiresExt3ClientAPI(function() {
            Ext.onReady(function() {
                var containerPath = LABKEY.container.path + '/FileRepository';
                var animalFolder = new LABKEY.FileSystem.WebdavFileSystem({baseUrl: LABKEY.ActionURL.getBaseURL() + '_webdav' + containerPath});
                var location = {id: animalIds};
                var panel = tab.add({id: 'filesDiv', style: 'margin-bottom:20px'});

                var handler = function (location) {
                    var webPart = new LABKEY.WebPart({
                        title: 'File Repository for ' + animalIds,
                        partName: 'Files',
                        renderTo: 'filesDiv-body',
                        containerPath: containerPath,
                        partConfig: {path: location},
                        success: function () {
                            panel.setHeight(450);
                        }
                    });
                    webPart.render();

                };

                animalFolder.listFiles({
                    success: function (filesystem, path, files) {
                        let missingCount = 0;
                        for (let i = 0; i < folders.length; i++) {
                            if (!files.find(file => file.id.indexOf(LABKEY.ActionURL.encodePath(folders[i])) !== -1)) {
                                missingCount++;
                            }
                        }
                        if (missingCount > 0) {
                            renderNotFound(panel, animalFolder, handler, location.id);
                        }
                        else {
                            handler(location.id);
                        }
                    },
                    path: "/@files/" + animalIds, // probably should make this more robust in the future, checking all folders.
                    failure: function () {
                        LABKEY.Security.getUserPermissions({
                            containerPath: containerPath,
                            success: function (userPermsInfo) {
                                var hasInsert = false;
                                for (var i = 0; i < userPermsInfo.container.effectivePermissions.length; i++) {
                                    if (userPermsInfo.container.effectivePermissions[i] == 'org.labkey.api.security.permissions.InsertPermission') {
                                        hasInsert = true;
                                    }
                                }
                                if (hasInsert) {
                                    renderNotFound(panel, animalFolder, handler, location.id);
                                }
                                else {
                                    panel.add({
                                        xtype: 'ldk-webpartpanel',
                                        title: 'File Repository for ' + animalIds,
                                        items: [
                                            {
                                                xtype: 'label',
                                                text: 'The current animal does not have any files, and you do not have permission to upload new files.'
                                            }]
                                    });
                                }
                            },
                            failure: function (error, response) {
                                var message;
                                if (response.status == 404) {
                                    message = 'The folder ' + containerPath + ' does not exist, so no files can be shown or uploaded. Contact EHR services to correct the configuration.';
                                }
                                else if (response.status == 401 || response.status == 403) {
                                    message = 'You do not have permission to upload or view files. Contact EHR services to get permission.';
                                }
                                else {
                                    message = 'There was an error attempting to load the file data: ' + response.status;
                                }
                                panel.add({
                                    xtype: 'ldk-webpartpanel',
                                    title: 'File Repository for ' + animalIds,
                                    items: [
                                        {
                                            xtype: 'label',
                                            text: message
                                        }]
                                });
                            }
                        });
                    },

                    forceReload: true
                });


                if (File && File.panel && File.panel.Browser && File.panel.Browser._pipelineConfigurationCache) {
                    File.panel.Browser._pipelineConfigurationCache = {};
                }
            });
        }, this);
    }
};
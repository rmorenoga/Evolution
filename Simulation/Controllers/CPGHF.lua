-- DO NOT WRITE CODE OUTSIDE OF THE if-then-end SECTIONS BELOW!! (unless the code is a function definition)



if (sim_call_type==sim_childscriptcall_initialization) then
    require("lua/modular/CPG")
    require("lua/modular/Horm")
    sensor = {}

	robot=simGetObjectHandle('North')
    Motor=simGetObjectHandle('centralMotor') 
    sensor[1]=simGetObjectHandle('psensorA')
    sensor[2]=simGetObjectHandle('psensorB')
    sensor[3]=simGetObjectHandle('psensorC')
    sensor[4]=simGetObjectHandle('psensorD')
    
    suffix= simGetNameSuffix(nil)
    myhandle = simGetScriptHandle(nil)
 
    connh = {}
    recdata = {}
    sensorR = {}
    sensorD = {}
    horm = {}
    rhorm = {}
    phorm = {}
    count = {0,0,0,0,0}

    if(simGetStringSignal('ModHandles')==nil) then
        index = -1
    else
        data = simGetStringSignal('ModHandles')
        modhandles = simUnpackInt32Table(data)
        index = -1
        for i=1,#modhandles do
            if (robot==modhandles[i]) then
                index = i
            end
        end
        --print(index..' '..modhandles[index])
    end
      
    if(simGetStringSignal('ConnHandles')==nil) then
        connh = {-1,-1,-1,-1}
    else
        data = simGetStringSignal('ConnHandles')
        conh = simUnpackInt32Table(data,4*(index-1),4)
        for i=1,#conh do
            if (conh[i] ~= -1) then
                connh[i] = simGetScriptAssociatedWithObject(conh[i])
            else
                connh[i] = -1
            end
        end
        --print(index)
        --for k,v in pairs(connh) do print(k,v) end    
    end
    
    for i=1,#sensor do
        sensorR[i]=0
        sensorD[i]=-1
    end
    
    for i=1,#sensor do
        sensorR[i] = simReadProximitySensor(sensor[i])
        if (sensorR[i]==1) then
            sensorR[i],sensorD[i] = simReadProximitySensor(sensor[i])
        end
    end
    
   
    for i=1,#connh do
        phorm[i] = {}
    end   

    -- CPG parameters        

    teta = {0,-1,-1,-1,-1}
    ampli = 0
    dampli = 0
    offset= 0
    doffset = 0
    dt = 0.05
    pfactor = 0.5
    v = 0.5
    wij = {}
    wij[1]= 7
    wij[2]= 7
    wij[3]= 7
    wij[4]= 7

    ampd = 0.5
    offd = 0
    phasediff = {}
    phasediff[1] = math.pi*pfactor
    phasediff[2]= math.pi*pfactor
    phasediff[3] = math.pi*pfactor
    phasediff[4]= math.pi*pfactor
    

    -- Hormone parameters
    proprob = 0.75 --actual prob 1-proprob
    baseprob = 0.25 --actual prob 1-baseprob

    if(simGetStringSignal('ExtraParam')==nil) then
        delta = 0.01
    else
        data = simGetStringSignal('ExtraParam')
        eparam = simUnpackFloatTable(data)
        delta = eparam[1]
        if(#eparam>1)then
            proprob = eparam[2]
            if(#eparam>2)then
                baseprob = eparam[3]
            end            
        end
        --print(delta)
    end
    --print(baseprob)
    --print(proprob)

    if(simGetStringSignal('ControlParam')==nil) then
        ampset = {}
        ampset[1] = 0.5
        ampset[2] = -0.7
        ampset[3] = 0.8
        ampset[4] = 0.1
        ampset[5] = -0.2

        offsetset = {}
        offsetset[1] = 0
        offsetset[2] = 0.1
        offsetset[3] = -0.7
        offsetset[4] = 0.3
        offsetset[5] = -0.6

        phasediffset = {}
        phasediffset[1] = {}
        phasediffset[1][1] = math.pi*0.5
        phasediffset[1][2] = math.pi*0.5
        phasediffset[1][3] = math.pi*0.5
        phasediffset[1][4] = math.pi*0.5
        phasediffset[2] = {}
        phasediffset[2][1] = math.pi*0.7
        phasediffset[2][2] = math.pi*0.5
        phasediffset[2][3] = math.pi*0.3
        phasediffset[2][4] = math.pi*0.1
        phasediffset[3] = {}
        phasediffset[3][1] = math.pi*-0.1
        phasediffset[3][2] = math.pi*0.5
        phasediffset[3][3] = math.pi*-0.6
        phasediffset[3][4] = math.pi*0.1
        phasediffset[4] = {}
        phasediffset[4][1] = math.pi*0.5
        phasediffset[4][2] = math.pi*-0.5
        phasediffset[4][3] = math.pi*0.6
        phasediffset[4][4] = math.pi*0.2
        phasediffset[5] = {}
        phasediffset[5][1] = math.pi*-0.6
        phasediffset[5][2] = math.pi*-0.5
        phasediffset[5][3] = math.pi*-0.8
        phasediffset[5][4] = math.pi*-0.1

        vset = {}
        vset[1] = 0.5
        vset[2] = 0.2
        vset[3] = 0.1
        vset[4] = 0.7
        vset[5] = 0.6
    else
        data = simGetStringSignal('ControlParam')
        param = simUnpackFloatTable(data,35*(index-1),35)
        print(index)
        --for k,v in pairs(param) do print(k,v) end
        ampset = {}
        ampset[1] = param[1]
        ampset[2] = param[2]
        ampset[3] = param[3]
        ampset[4] = param[4]
        ampset[5] = param[5]

        offsetset = {}
        offsetset[1] = param[6]
        offsetset[2] = param[7]
        offsetset[3] = param[8]
        offsetset[4] = param[9]
        offsetset[5] = param[10]

        phasediffset = {}
        phasediffset[1] = {}
        phasediffset[1][1] = param[11]
        phasediffset[1][2] = param[12]
        phasediffset[1][3] = param[13]
        phasediffset[1][4] = param[14]
        phasediffset[2] = {}
        phasediffset[2][1] = param[15]
        phasediffset[2][2] = param[16]
        phasediffset[2][3] = param[17]
        phasediffset[2][4] = param[18]
        phasediffset[3] = {}
        phasediffset[3][1] = param[19]
        phasediffset[3][2] = param[20]
        phasediffset[3][3] = param[21]
        phasediffset[3][4] = param[22]
        phasediffset[4] = {}
        phasediffset[4][1] = param[23]
        phasediffset[4][2] = param[24]
        phasediffset[4][3] = param[25]
        phasediffset[4][4] = param[26]
        phasediffset[5] = {}
        phasediffset[5][1] = param[27]
        phasediffset[5][2] = param[28]
        phasediffset[5][3] = param[29]
        phasediffset[5][4] = param[30]
        vset = {}
        vset[1] = param[31]
        vset[2] = param[32]
        vset[3] = param[33]
        vset[4] = param[34]
        vset[5] = param[35]
    end

    
    
    

end


if (sim_call_type==sim_childscriptcall_actuation) then
    for k in pairs(horm) do horm[k] = nil end
    
    horm,sendhorm = ghormone(connh,sensorR,sensorD,baseprob)
    count = integrate(horm,count)

    r = math.random()

    ampd,offd,phasediff,v = receptorsf(horm,ampd,offd,phasediff,v,ampset,offsetset,phasediffset,vset,delta,count)
    horm[1] = -1

    --Pack hormone for sending
    hormp = simPackFloatTable(horm)
    
    --print('********* '.. suffix)
    --for k,v in pairs(teta) do print(k,v) end
    --UpdateCPG
    
    teta[1],ampli,dampli,offset,doffset = updateCPG(teta,ampli,dampli,offset,doffset,dt,ampd,offd,phasediff,v,wij)

    angle = (math.pi/2)*(offset+(math.cos(teta[1])*ampli))
    simSetJointTargetPosition(Motor,angle)

    for i=1,#connh do
        if (connh[i]~=-1) then
            simSendData(connh[i],1038,'phase',teta[1])
            --print('Sent phase in '..myhandle..' suffix '..suffix..' to '..connh[i])
            if(sendhorm) then
                simSendData(connh[i],1038,'horm',hormp)
                --print('Sent hormone in '..myhandle..' suffix '..suffix..' to '..connh[i])
            end
            for j=1,#connh do
                if (j~=i) then
                    if (connh[j]~=-1 and #phorm[j]>0) then
                        for k=1,#phorm[j] do
                            if (propagate(proprob)) then
                                simSendData(connh[i],1038,'horm',phorm[j][k])
                                --print('Propagated hormone received from '..connh[j]..' in '..myhandle..' to '..connh[i])
                            end
                        end
                    end
                end
            end
                
        end
    end

end


if (sim_call_type==sim_childscriptcall_sensing) then

    for k in pairs(sensorR) do sensorR[k]=0 end
    for k in pairs(sensorD) do sensorD[k]=-1 end

    for i=1,#sensor do
        sensorR[i] = simReadProximitySensor(sensor[i])
        if (sensorR[i]==1) then
            sensorR[i],sensorD[i] = simReadProximitySensor(sensor[i])
        end
    end

    
    for k in pairs(phorm) do
        for i in pairs(phorm[k]) do
            phorm[k][i]=nil
        end
    end
    

    data,sender = simReceiveData(1038,'horm')
    while (data ~= nil) do
        for i=1,#connh do
            if (connh[i]==sender) then
                table.insert(phorm[i],data)
            end
        end
        --print('Received hormone in '..myhandle..' from'..sender)
        rhorm = simUnpackFloatTable(data)
        count = integrate(rhorm,count)
        ampd,offd,phasediff,v = receptorsf(horm,ampd,offd,phasediff,v,ampset,offsetset,phasediffset,vset,delta,count)
        data,sender = simReceiveData(1038,'horm')
    end
   
    
    

    for k in pairs(recdata) do recdata[k]=nil end
    for i=2,#teta do teta[i]=-1 end

    data,sender = simReceiveData(1038,'phase')
    while (data ~= nil) do
        recdata[sender] = data
        --print('Received phase in '..myhandle..' from'..sender)
        data,sender = simReceiveData(1038,'phase')
    end

    for k in pairs(recdata) do
        for i=1,#connh do
            if (connh[i]==k) then
            teta[i+1]=recdata[k]
            end
        end
    end
    
end


if (sim_call_type==sim_childscriptcall_cleanup) then

	-- Put some restoration code here

end

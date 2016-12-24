function getTPoints(mseq,Width)
    local TPoints={}
    local angle = 0
    local pos = {0,0}
    local nextpos = {0,0}

    for i=1,#mseq,1 do
        TPoints[i] = {}
        if(mseq[i]=='s') then
            
            TPoints[i][1] = pos[1] --Input X
            TPoints[i][2] = pos[2] --Input Y
        
            nextpos[1] = 0*math.cos(angle)-2*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+2*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

            TPoints[i][3] = pos[1] --Output X
            TPoints[i][4] = pos[2] --Output Y
            TPoints[i][5] = angle --Input Angle
            TPoints[i][6] = angle --Output Angle
          
            TPoints[i][7] = 2 --Distance added by the current part

        elseif(mseq[i]=='b') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y    
        
            nextpos[1] = 0*math.cos(angle)-4*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+4*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle
        TPoints[i][6] = angle --Output Angle

        TPoints[i][7] = 4 --Distance added by the current part


        elseif(mseq[i]=='r') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y       
          
    
            xo = (Width/2)+0.075+0.625
            yo = (Width/2) + 0.7
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]
        
         TPoints[i][3] = pos[1] --Output X
         TPoints[i][4] = pos[2] --Output Y
         TPoints[i][5] = angle --Input Angle

         angle = angle - math.pi/2
         if(angle < -math.pi) then
            angle = math.pi/2
         end
    
         TPoints[i][6] = angle --Output Angle
         --TPoints[i][7] = 2 --Distance added by the current part
         TPoints[i][7] = xo + yo

        elseif(mseq[i]=='l') then

            
        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y
            
            xo = -(Width/2)-0.075-0.625
            yo = (Width/2) + 0.7
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle

            angle = angle + math.pi/2 
            if(angle > math.pi) then
                angle = -math.pi/2
            end

        TPoints[i][6] = angle --Output Angle
        --TPoints[i][7] = 2 --Distance added by the current part
        TPoints[i][7] = -xo + yo
        end
    end
    return TPoints
end

function getDistance(CurrentTPart,TPoints,seqlength,position)
    local Goal = false
    local Dxo = 0
    local Dyo = 0
    local sum = 0
    local D = 0
    local Dout = 0

    if(CurrentTPart >= 1) then
        
        -- Check if robot got out of the last part
        if(CurrentTPart>seqlength) then
            Goal = true
        else
            -- Check if over output point taking into account output angle
            if(TPoints[CurrentTPart][6] == 0) then
                if(position[2]>TPoints[CurrentTPart][4]) then
                    CurrentTPart = CurrentTPart + 1
                end
            elseif(TPoints[CurrentTPart][6] == math.pi/2) then
                if(position[1]<TPoints[CurrentTPart][3]) then
                    CurrentTPart = CurrentTPart + 1
                end
            elseif(TPoints[CurrentTPart][6] == -math.pi/2) then
                if(position[1]>TPoints[CurrentTPart][3]) then
                    CurrentTPart = CurrentTPart + 1
                end
            elseif(TPoints[CurrentTPart][6] == math.pi or TPoints[CurrentTPart][6] == -math.pi) then
                if(position[2]<TPoints[CurrentTPart][4]) then
                    CurrentTPart = CurrentTPart + 1
                end
            end
        end
        
        
        if(CurrentTPart<=seqlength) then
            -- Check if over input point taking into account input angle
            if(TPoints[CurrentTPart][5] == 0) then
                if(position[2]<TPoints[CurrentTPart][2]) then
                    CurrentTPart = CurrentTPart - 1
                end
            elseif(TPoints[CurrentTPart][5] == math.pi/2) then
                if(position[1]>TPoints[CurrentTPart][1]) then
                    CurrentTPart = CurrentTPart - 1
                end
            elseif(TPoints[CurrentTPart][5] == -math.pi/2) then
                if(position[1]<TPoints[CurrentTPart][1]) then
                    CurrentTPart = CurrentTPart - 1
                end
            elseif(TPoints[CurrentTPart][5] == math.pi or TPoints[CurrentTPart][5] == -math.pi) then
                if(position[2]>TPoints[CurrentTPart][2]) then
                    CurrentTPart = CurrentTPart - 1
                end
            end
        end

    else

        if(position[2]>=0) then
            CurrentTPart = CurrentTPart + 1
        end
    
    end
        
    if(CurrentTPart>=1) then
        if(CurrentTPart<=#mseq) then
            Dxo =  TPoints[CurrentTPart][3]-position[1]
            Dyo =  TPoints[CurrentTPart][4]-position[2]
            Do = math.sqrt((Dxo*Dxo)+(Dyo*Dyo))
            
            sum = 0
            for i=CurrentTPart+1,#mseq,1 do
                sum = sum + TPoints[i][7]
            end
            D = Do + sum
            --print(sum)
            --print(D)
        end
    else
        Dxo =  0-position[1]
        Dyo =  0-position[2]
        Do = math.sqrt((Dxo*Dxo)+(Dyo*Dyo))
            
        sum = 0
        for i=1,#mseq,1 do
            sum = sum + TPoints[i][7]
        end
        --print(sum)
        D = Do + sum
        --print(D)
    end
    -- Normalize Distance
    sum = 0
    for i=1,seqlength,1 do
        sum = sum + TPoints[i][7]
    end
    Dout = D/sum
    if(Dout>1) then
        Dout = 1
    end

    return CurrentTPart,Goal,Dout
end